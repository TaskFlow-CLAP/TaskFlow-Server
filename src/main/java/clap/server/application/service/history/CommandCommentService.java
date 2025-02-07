package clap.server.application.service.history;

import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;
import clap.server.application.port.inbound.domain.CommentService;
import clap.server.application.port.inbound.history.DeleteCommentUsecase;
import clap.server.application.port.inbound.history.EditCommentUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Comment;
import clap.server.exception.ApplicationException;
import clap.server.exception.DomainException;
import clap.server.exception.code.CommentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

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

        if (Member.checkCommenter(comment.getTask(), member)) {
            comment.updateComment(request.content());
            commandCommentPort.saveComment(comment);
        };
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        Member member = memberService.findActiveMember(userId);
        Comment comment = commentService.findById(commentId);

        if (Member.checkCommenter(comment.getTask(), member)) {
            // 첨부파일이 있을 경우 삭제
            if (loadAttachmentPort.exitsByCommentId(commentId)) {
                deleteAttachments(commentId);
            }
            // comment 삭제
            commandCommentPort.deleteComment(comment);
            // comment와 관련된 taskHistory도 함께 삭제

            commandTaskHistoryPort.deleteTaskHistoryByCommentId(commentId);
        }
    }

    private void deleteAttachments(Long commentId) {
        Attachment attachment = loadAttachmentPort.findByCommentId(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_ATTACHMENT_NOT_FOUND));
        attachment.softDelete();
        commandAttachmentPort.save(attachment);
    }
}
