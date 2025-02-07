package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.CreateTaskResponse;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.response.TaskResponseMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.policy.attachment.FilePathPolicyConstants;
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

        int fileSize = files == null ? 0 : files.size();
        Task task = Task.createTask(member, category, createTaskRequest.title(), createTaskRequest.description(), fileSize);
        Task savedTask = commandTaskPort.save(task);

        if (files != null) {
            fileSize = saveAttachments(files, savedTask);
        }
        savedTask.finalSave(fileSize);
        commandTaskPort.save(savedTask);

        publishNotification(savedTask);
        return TaskResponseMapper.toCreateTaskResponse(savedTask);
    }

    private int saveAttachments(List<MultipartFile> files, Task task) {
        List<String> fileUrls = s3UploadPort.uploadFiles(FilePathPolicyConstants.TASK_FILE, files);
        List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
        return fileUrls.size();
    }

    private void publishNotification(Task task) {
        List<Member> reviewers = memberService.findReviewers();
        reviewers.forEach(reviewer -> {
            boolean isManager = reviewer.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
            sendNotificationService.sendPushNotification(reviewer, NotificationType.TASK_REQUESTED,
                    task, null, null, isManager);
        });

        sendNotificationService.sendAgitNotification(NotificationType.TASK_REQUESTED,
                task, null, null);
    }
}