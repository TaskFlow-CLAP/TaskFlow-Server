package clap.server.application.service.history;

import clap.server.adapter.inbound.web.dto.history.request.EditCommentRequest;
import clap.server.application.port.inbound.history.DeleteCommentUsecase;
import clap.server.application.port.inbound.history.EditCommentUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.task.LoadCommentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Comment;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class CommandCommentService implements EditCommentUsecase, DeleteCommentUsecase {

    private final MemberService memberService;
    private final LoadCommentPort loadCommentPort;
    private final CommandCommentPort commandCommentPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final CommandAttachmentPort commandAttachmentPort;

    @Transactional
    @Override
    public void editComment(Long userId, Long commentId, EditCommentRequest request) {

        Member member = memberService.findActiveMember(userId);

        Comment comment = loadCommentPort.findById(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (Member.checkCommenter(comment.getTask(), member)) {

            comment.updateComment(request.content());
            commandCommentPort.saveComment(comment);
        };
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        Member member = memberService.findActiveMember(userId);


        Comment comment = loadCommentPort.findById(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (Member.checkCommenter(comment.getTask(), member)) {
            if (loadAttachmentPort.exitsByCommentId(commentId)) {
                deleteAttachments(commentId);
            }
            commandCommentPort.deleteComment(comment);
        };
    }

    private void deleteAttachments(Long commentId) {
        Attachment attachment = loadAttachmentPort.findByCommentId(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_ATTACHMENT_NOT_FOUND));
        attachment.softDelete();
        commandAttachmentPort.save(attachment);
    }
}
