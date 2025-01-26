package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.notification.SseRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;

import clap.server.adapter.inbound.web.dto.webhook.SendAgitRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendKakaoWorkRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;
import clap.server.adapter.outbound.infrastructure.s3.S3UploadAdapter;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;

import clap.server.application.service.webhook.SendKaKaoWorkService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.FilePath;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static clap.server.domain.model.notification.Notification.createTaskNotification;


@ApplicationService
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final CommandTaskPort commandTaskPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final S3UploadAdapter s3UploadAdapter;
    private final SendKaKaoWorkService sendKaKaoWorkService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public CreateTaskResponse createTask(Long requesterId, CreateTaskRequest createTaskRequest, List<MultipartFile> files) {
        Member member = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(createTaskRequest.categoryId());
        Task task = Task.createTask(member, category, createTaskRequest.title(), createTaskRequest.description());
        Task savedTask = commandTaskPort.save(task);

        saveAttachments(files, savedTask);
        publishNotification(savedTask);
        return TaskMapper.toCreateTaskResponse(savedTask);
    }

    private void saveAttachments(List<MultipartFile> files, Task task) {
        List<String> fileUrls = s3UploadAdapter.uploadFiles(FilePath.TASK_IMAGE, files);
        List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
    }

    private void publishNotification(Task task){
        List<Member> reviewers = memberService.findReviewers();

        // 검토자들 각각에 대한 알림 생성 후 event 발행
        for (Member reviewer : reviewers) {
            // 알림 저장
            Notification notification = createTaskNotification(task, reviewer, NotificationType.TASK_REQUESTED);
            applicationEventPublisher.publishEvent(notification);

            // SSE 실시간 알림 전송
            SseRequest sseRequest = new SseRequest(
                    notification.getTask().getTitle(),
                    notification.getType(),
                    reviewer.getMemberId(),
                    null
            );
            applicationEventPublisher.publishEvent(sseRequest);

            //Email Webhook 실시간 전송
            SendWebhookRequest sendWebhookRequest = new SendWebhookRequest(
                    reviewer.getMemberInfo().getEmail(),
                    NotificationType.TASK_REQUESTED,
                    task.getTitle(),
                    task.getRequester().getNickname(),
                    null,
                    null
            );
            applicationEventPublisher.publishEvent(sendWebhookRequest);

            //Kakao Webhook 실시간 전송
            SendKakaoWorkRequest sendKakaoWorkRequest = new SendKakaoWorkRequest(
                    reviewer.getMemberInfo().getEmail(),
                    NotificationType.TASK_REQUESTED,
                    task.getTitle(),
                    task.getRequester().getNickname(),
                    null,
                    null
            );
            applicationEventPublisher.publishEvent(sendKakaoWorkRequest);

            //아지트 Webhook 실시간 전송
            SendAgitRequest sendAgitRequest = new SendAgitRequest(
                    reviewer.getMemberInfo().getEmail(),
                    NotificationType.TASK_REQUESTED,
                    task.getTitle(),
                    task.getRequester().getNickname(),
                    null,
                    null
            );
            applicationEventPublisher.publishEvent(sendAgitRequest);
        }
    }

}