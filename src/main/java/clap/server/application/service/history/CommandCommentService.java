package clap.server.application.service.history;

import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;
import clap.server.application.port.inbound.domain.CommentService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.history.DeleteCommentUsecase;
import clap.server.application.port.inbound.history.EditCommentUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Comment;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class CommandCommentService implements EditCommentUsecase, DeleteCommentUsecase {

    private final MemberService memberService;
    private final CommentService commentService;

    private final CommandCommentPort commandCommentPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final CommandTaskHistoryPort commandTaskHistoryPort;

    @Transactional
    @Override
    public void editComment(Long userId, Long commentId, EditCommentRequest request) {
        Member member = memberService.findActiveMember(userId);
        Comment comment = commentService.findById(commentId);

        if (comment.getMember().getMemberId().equals(member.getMemberId())) {
            comment.updateComment(request.content());
            commandCommentPort.saveComment(comment);
        }
        else throw new ApplicationException(CommentErrorCode.NOT_A_COMMENT_WRITER);

    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        Member member = memberService.findActiveMember(userId);
        Comment comment = commentService.findById(commentId);

        if (comment.getMember().getMemberId().equals(member.getMemberId())) {
            if (loadAttachmentPort.exitsByCommentId(commentId)) {
                deleteAttachments(commentId);
            }
            commandCommentPort.deleteComment(comment);
            commandTaskHistoryPort.deleteTaskHistoryByCommentId(commentId);
        }
        else throw new ApplicationException(CommentErrorCode.NOT_A_COMMENT_WRITER);
    }

    private void deleteAttachments(Long commentId) {
        Attachment attachment = loadAttachmentPort.findByCommentId(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_ATTACHMENT_NOT_FOUND));
        attachment.softDelete();
        commandAttachmentPort.save(attachment);
    }
}
