package clap.server.application.service.history;

import clap.server.adapter.inbound.web.dto.history.request.CreateCommentRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.history.SaveCommentAttachmentUsecase;
import clap.server.application.port.inbound.history.SaveCommentUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import clap.server.domain.policy.attachment.FilePathPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@ApplicationService
@RequiredArgsConstructor
public class PostCommentService implements SaveCommentUsecase, SaveCommentAttachmentUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CommandCommentPort commandCommentPort;
    private final S3UploadPort s3UploadPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final SendNotificationService sendNotificationService;

    @Transactional
    @Override
    public void save(Long userId, Long taskId, CreateCommentRequest request) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(userId);

        // 일반 회원일 경우 => 요청자인지 확인
        if (Member.checkCommenter(task, member)) {
            Comment comment = Comment.createComment(member, task, request.content());
            Comment savedComment = commandCommentPort.saveComment(comment);

            TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.COMMENT, task, null, member, savedComment);
            commandTaskHistoryPort.save(taskHistory);

            Member processor = task.getProcessor();
            Member requester = task.getRequester();
            if (member.getMemberInfo().getRole() == MemberRole.ROLE_USER) {
                publishNotification(processor, task, request.content(), requester.getNickname());
            } else {
                publishNotification(requester, task, request.content(), processor.getNickname());
            }
        }
    }

    @Transactional
    @Override
    public void saveCommentAttachment(Long userId, Long taskId, MultipartFile file) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(userId);

        if (Member.checkCommenter(task, member)) {
            Comment comment = Comment.createComment(member, task, null);
            Comment savedComment = commandCommentPort.saveComment(comment);
            String fileName = saveAttachment(file, task, savedComment);

            TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.COMMENT_FILE, task, null, member, savedComment);
            commandTaskHistoryPort.save(taskHistory);

            Member processor = task.getProcessor();
            Member requester = task.getRequester();
            if (member.getMemberInfo().getRole() == requester.getMemberInfo().getRole()) {
                publishNotification(processor, task, fileName + "(첨부파일)", requester.getNickname());
            } else {
                publishNotification(requester, task, fileName + "(첨부파일)", processor.getNickname());
            }
        }
    }

    private String saveAttachment(MultipartFile file, Task task, Comment comment) {
        String fileUrl = s3UploadPort.uploadSingleFile(FilePathPolicy.TASK_COMMENT, file);
        Attachment attachment = Attachment.createCommentAttachment(task, comment, file.getOriginalFilename(), fileUrl, file.getSize());
        commandAttachmentPort.save(attachment);
        return file.getOriginalFilename();
    }

    private void publishNotification(Member receiver, Task task, String message, String commenterName) {
        sendNotificationService.sendPushNotification(receiver, NotificationType.COMMENT, task, message, commenterName);
    }
}
