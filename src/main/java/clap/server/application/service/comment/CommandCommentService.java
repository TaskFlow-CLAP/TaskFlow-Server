package clap.server.application.service.comment;

import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.application.port.inbound.comment.CommandCommentUsecase;
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
public class CommandCommentService implements CommandCommentUsecase {

    private final MemberService memberService;
    private final LoadCommentPort loadCommentPort;
    private final CommandCommentPort commandCommentPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final CommandAttachmentPort commandAttachmentPort;

    @Transactional
    @Override
    public void updateComment(Long userId, Long commentId, PostAndEditCommentRequest request) {

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
