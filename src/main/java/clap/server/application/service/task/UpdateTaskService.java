package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskLabelRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskProcessorRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskLabelUsecase;
import clap.server.application.port.inbound.task.UpdateTaskProcessorUsecase;
import clap.server.application.port.inbound.task.UpdateTaskStatusUsecase;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.service.webhook.SendNotificationService;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.*;
import clap.server.common.constants.FilePathConstants;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static clap.server.domain.policy.task.TaskPolicyConstants.TASK_MAX_FILE_COUNT;
import static clap.server.domain.policy.task.TaskPolicyConstants.TASK_UPDATABLE_STATUS;


@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class UpdateTaskService implements UpdateTaskUsecase, UpdateTaskStatusUsecase, UpdateTaskProcessorUsecase, UpdateTaskLabelUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TaskService taskService;
    private final SendNotificationService sendNotificationService;

    private final LoadAttachmentPort loadAttachmentPort;
    private final LabelService labelService;
    private final CommandAttachmentPort commandAttachmentPort;
    private final CommandTaskHistoryPort commandTaskHistoryPort;
    private final S3UploadPort s3UploadPort;

    @Override
    @Transactional
    public void updateTask(Long requesterId, Long taskId, UpdateTaskRequest request, List<MultipartFile> files) {
        memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(request.categoryId());
        Task task = taskService.findById(taskId);

        int attachmentToAdd = files==null? 0 : files.size();
        int attachmentCount = task.getAttachmentCount() - request.attachmentsToDelete().size() + attachmentToAdd;
        if (attachmentCount > TASK_MAX_FILE_COUNT) {
            throw new ApplicationException(TaskErrorCode.FILE_COUNT_EXCEEDED);
        }
        if (!request.attachmentsToDelete().isEmpty()) {
            updateAttachments(request.attachmentsToDelete(), files, task);
        }
        task.updateTask(requesterId, category, request.title(), request.description(), attachmentCount);
        taskService.upsert(task);
    }

    @Override
    @Transactional
    public void updateTaskStatus(Long memberId, Long taskId, TaskStatus taskStatus) {
        memberService.findActiveMember(memberId);
        Task task = taskService.findById(taskId);

        if (!TASK_UPDATABLE_STATUS.contains(taskStatus)) {
            throw new ApplicationException(TaskErrorCode.TASK_STATUS_NOT_ALLOWED);
        }

        if (!task.getTaskStatus().equals(taskStatus)) {
            task.updateTaskStatus(taskStatus);
            Task updateTask = taskService.upsert(task);
            TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.STATUS_SWITCHED, task, taskStatus.getDescription(), null, null);
            commandTaskHistoryPort.save(taskHistory);

            List<Member> receivers = List.of(task.getRequester(), task.getProcessor());
            publishNotification(receivers, updateTask, NotificationType.STATUS_SWITCHED, String.valueOf(updateTask.getTaskStatus()));
        }
    }

    @Transactional
    @Override
    public void updateTaskProcessor(Long taskId, Long memberId, UpdateTaskProcessorRequest request) {
        memberService.findActiveMember(memberId);

        Task task = taskService.findById(taskId);
        Member processor = memberService.findById(request.processorId());

        task.updateProcessor(processor);
        Task updateTask = taskService.upsert(task);
        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.PROCESSOR_CHANGED, task, null, processor, null);
        commandTaskHistoryPort.save(taskHistory);

        List<Member> receivers = List.of(updateTask.getRequester(), updateTask.getProcessor());
        publishNotification(receivers, updateTask, NotificationType.PROCESSOR_CHANGED, processor.getNickname());
    }

    @Transactional
    @Override
    public void updateTaskLabel(Long taskId, Long memberId, UpdateTaskLabelRequest request) {
        memberService.findActiveMember(memberId);
        memberService.findReviewer(memberId);
        Task task = taskService.findById(taskId);
        Label label = labelService.findById(request.labelId());

        task.updateLabel(label);
        taskService.upsert(task);
    }

    private void updateAttachments(List<Long> attachmentIdsToDelete, List<MultipartFile> files, Task task) {
        List<Attachment> attachmentsToDelete = validateAndGetAttachments(attachmentIdsToDelete, task);
        attachmentsToDelete.stream()
                .peek(Attachment::softDelete)
                .forEach(commandAttachmentPort::save);

        if (files != null) {
            List<String> fileUrls = s3UploadPort.uploadFiles(FilePathConstants.TASK_FILE, files);
            List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
            commandAttachmentPort.saveAll(attachments);
        }

    }

    private List<Attachment> validateAndGetAttachments(List<Long> attachmentIdsToDelete, Task task) {
        List<Attachment> attachmentsOfTask = loadAttachmentPort.findAllByTaskIdAndCommentIsNullAndAttachmentId(task.getTaskId(), attachmentIdsToDelete);
        if (attachmentsOfTask.size() != attachmentIdsToDelete.size()) {
            throw new ApplicationException(TaskErrorCode.TASK_ATTACHMENT_NOT_FOUND);
        }
        return attachmentsOfTask;
    }

    private void publishNotification(List<Member> receivers, Task task, NotificationType notificationType, String message) {
        receivers.forEach(receiver -> {
            boolean isManager = receiver.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER;
            sendNotificationService.sendPushNotification(receiver, notificationType,
                    task, message, null, isManager);
        });

        sendNotificationService.sendAgitNotification(notificationType, task, message, null);
    }
}
