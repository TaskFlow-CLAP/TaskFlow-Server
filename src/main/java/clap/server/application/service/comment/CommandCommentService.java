package clap.server.application.service.comment;

import clap.server.adapter.inbound.web.dto.task.DeleteCommentRequest;
import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
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
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.CommentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        if (checkCommenter(comment.getTask(), member)) {

            comment.updateComment(request.content());
            commandCommentPort.saveComment(comment);
        };
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId, DeleteCommentRequest request) {
        Member member = memberService.findActiveMember(userId);


        Comment comment = loadCommentPort.findById(commentId)
                .orElseThrow(() -> new ApplicationException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (checkCommenter(comment.getTask(), member)) {

            // 삭제할 댓글이 첨부파일일 경우
            if (!request.attachmentsToDelete().isEmpty()) {
                deleteAttachments(request.attachmentsToDelete(), comment.getTask(), comment.getCommentId());
            }

            comment.softDelete();
            commandCommentPort.saveComment(comment);
        };
    }

    private void deleteAttachments(List<Long> attachmentIdsToDelete, Task task, Long commentId) {
        List<Attachment> attachmentsToDelete = validateAndGetAttachments(attachmentIdsToDelete, task, commentId);
        attachmentsToDelete.forEach(Attachment::softDelete);
        commandAttachmentPort.saveAll(attachmentsToDelete);
    }

    private List<Attachment> validateAndGetAttachments(List<Long> attachmentIdsToDelete, Task task, Long commentId) {
        List<Attachment> attachmentsOfTask = loadAttachmentPort.findAllyByTaskIdAndCommentIdAndAttachmentId(task.getTaskId(), commentId, attachmentIdsToDelete);
        if (attachmentsOfTask.size() != attachmentIdsToDelete.size()) {
            throw new ApplicationException(TaskErrorCode.TASK_ATTACHMENT_NOT_FOUND);
        }
        return attachmentsOfTask;
    }

    public Boolean checkCommenter(Task task, Member member) {
        // 일반 회원일 경우 => 요청자인지 확인
        // 담당자일 경우 => 처리자인지 확인
        if ((member.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER)
                && !(member.getMemberId() == task.getProcessor().getMemberId())) {
            throw new ApplicationException(MemberErrorCode.NOT_A_COMMENTER);
        }

        else if ((member.getMemberInfo().getRole() == MemberRole.ROLE_USER)
                && !(member.getMemberId() == task.getRequester().getMemberId())) {
            throw new ApplicationException(MemberErrorCode.NOT_A_COMMENTER);
        }
        else {
            return true;
        }
    }
}
