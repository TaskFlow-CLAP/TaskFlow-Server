package clap.server.application.service.comment;

import clap.server.adapter.inbound.web.dto.notification.SseRequest;
import clap.server.adapter.inbound.web.dto.task.PostAndEditCommentRequest;
import clap.server.adapter.outbound.infrastructure.s3.S3UploadAdapter;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.port.inbound.comment.PostCommentUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandCommentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.notification.SendWebhookService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.constants.FilePathConstants;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.Task;
import clap.server.domain.model.task.TaskHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static clap.server.domain.model.notification.Notification.createTaskNotification;

@ApplicationService
@RequiredArgsConstructor
public class PostCommentService implements PostCommentUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CommandCommentPort commandCommentPort;
    private final S3UploadAdapter s3UploadAdapter;
    private final CommandAttachmentPort commandAttachmentPort;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SendWebhookService sendWebhookService;

    @Transactional
    @Override
    public void save(Long userId, Long taskId, PostAndEditCommentRequest request) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(userId);

        // 일반 회원일 경우 => 요청자인지 확인
        // 담당자일 경우 => 처리자인지 확인
        if (Member.checkCommenter(task, member)) {
            Comment comment = Comment.createComment(member, task, request.content());
            Comment savedComment = commandCommentPort.saveComment(comment);

            TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.COMMENT, task, null, member, savedComment);
            commandTaskHistoryPort.save(taskHistory);

            if (member.getMemberInfo().getRole() == MemberRole.ROLE_USER) {
                publishNotification(task.getProcessor(), task, comment.getContent(), member.getNickname());
            }
            else {
                publishNotification(task.getRequester(), task, comment.getContent(), task.getProcessor().getNickname());
            }
        }
    }

    @Transactional
    @Override
    public void saveCommentAttachment(Long userId, Long taskId, List<MultipartFile> files) {
        Task task = taskService.findById(taskId);
        Member member = memberService.findActiveMember(userId);

        if (Member.checkCommenter(task, member)) {
            Comment comment = Comment.createComment(member, task, null);
            Comment comment = Comment.createComment(member, task, "Attachment");
            Comment savedComment = commandCommentPort.saveComment(comment);
            saveAttachment(files, task, savedComment);

            TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.COMMENT_FILE, task, null, member, savedComment);
            commandTaskHistoryPort.save(taskHistory);

            if (member.getMemberInfo().getRole() == MemberRole.ROLE_USER) {
                publishNotification(task.getProcessor(), task, "첨부파일", member.getNickname());
            }
            else {
                publishNotification(task.getRequester(), task, "첨부파일", task.getProcessor().getNickname());
            }
        }
    }

    private void saveAttachment(List<MultipartFile> files, Task task, Comment comment) {
        List<String> fileUrls = s3UploadAdapter.uploadFiles(FilePathConstants.TASK_IMAGE, files);
        List<Attachment> attachments = AttachmentMapper.toCommentAttachments(task, comment, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
    }

    private void publishNotification(Member receiver, Task task, String message, String commenterName){
            // 알림 저장
        Notification notification = createTaskNotification(task, receiver, NotificationType.COMMENT);
        applicationEventPublisher.publishEvent(notification);

            // SSE 실시간 알림 전송
        SseRequest sseRequest = new SseRequest(
                notification.getTask().getTitle(),
                notification.getType(),
                receiver.getMemberId(),
                message
        );
        applicationEventPublisher.publishEvent(sseRequest);

        sendWebhookService.sendWebhookNotification(receiver, NotificationType.COMMENT, task, message, commenterName);
    }
}
