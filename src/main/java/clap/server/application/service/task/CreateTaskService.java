package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.CreateTaskResponse;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.TaskResponseMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.policy.attachment.FilePathPolicy;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@ApplicationService
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final CommandTaskPort commandTaskPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final S3UploadPort s3UploadPort;
    private final SendNotificationService sendNotificationService;

    @Override
    @Transactional
    public CreateTaskResponse createTask(Long requesterId, CreateTaskRequest createTaskRequest, List<MultipartFile> files) {
        Member member = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(createTaskRequest.categoryId());
        Task task = Task.createTask(member, category, createTaskRequest.title(), createTaskRequest.description());
        Task savedTask = commandTaskPort.save(task);
        savedTask.setInitialProcessorOrder();
        commandTaskPort.save(savedTask);

        if (files != null) {
            saveAttachments(files, savedTask);}
        publishNotification(savedTask, savedTask.getTitle());
        return TaskResponseMapper.toCreateTaskResponse(savedTask);
    }

    private void saveAttachments(List<MultipartFile> files, Task task) {
        List<String> fileUrls = s3UploadPort.uploadFiles(FilePathPolicy.TASK_IMAGE, files);
        List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
    }

    private void publishNotification(Task task, String taskTitle) {
        List<Member> reviewers = memberService.findReviewers();
        reviewers.forEach(reviewer -> {sendNotificationService.sendPushNotification(reviewer, reviewer.getMemberInfo().getNickname(), NotificationType.TASK_REQUESTED,
                task, taskTitle, null, null);});

        sendNotificationService.sendAgitNotification(NotificationType.TASK_REQUESTED,
                task, taskTitle, null, null);
    }
}