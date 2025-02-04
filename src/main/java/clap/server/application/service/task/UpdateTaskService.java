package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskLabelRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskProcessorRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.UpdateTaskResponse;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.TaskResponseMapper;
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
import clap.server.domain.model.task.*;
import clap.server.domain.policy.attachment.FilePathPolicy;
import clap.server.domain.model.member.Member;
import clap.server.domain.policy.task.TaskPolicyConstants;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.*;
import clap.server.domain.policy.attachment.FilePathPolicy;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.NotificationErrorCode;
import clap.server.exception.code.TaskErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public UpdateTaskResponse updateTask(Long requesterId, Long taskId, UpdateTaskRequest updateTaskRequest, List<MultipartFile> files) {
        Member requester = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(updateTaskRequest.categoryId());
        Task task = taskService.findById(taskId);

        task.updateTask(requesterId, category, updateTaskRequest.title(), updateTaskRequest.description());
        Task updatedTask = taskService.upsert(task);

        if (!updateTaskRequest.attachmentsToDelete().isEmpty()) {
            updateAttachments(updateTaskRequest.attachmentsToDelete(), files, task);
        }
        return TaskResponseMapper.toUpdateTaskResponse(updatedTask);
    }

    @Override
    @Transactional
    public void updateTaskStatus(Long memberId, Long taskId, TaskStatus taskStatus) {
        memberService.findActiveMember(memberId);
        memberService.findReviewer(memberId);
        Task task = taskService.findById(taskId);

        if(!task.getTaskStatus().equals(taskStatus)){
            task.updateTaskStatus(taskStatus);
            Task updateTask = taskService.upsert(task);
            TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.STATUS_SWITCHED, task, taskStatus.getDescription(), null,null);
            commandTaskHistoryPort.save(taskHistory);
            publishNotification(updateTask, NotificationType.STATUS_SWITCHED, String.valueOf(updateTask.getTaskStatus()));
        }
    }

    @Transactional
    @Override
    public UpdateTaskResponse updateTaskProcessor(Long taskId, Long userId, UpdateTaskProcessorRequest request) {
        memberService.findActiveMember(userId);
        memberService.findReviewer(userId);
        Member processor = memberService.findById(request.processorId());

        Task task = taskService.findById(taskId);
        task.updateProcessor(processor);
        Task updateTask = taskService.upsert(task);

        publishNotification(updateTask, NotificationType.PROCESSOR_CHANGED, updateTask.getProcessor().getNickname());
        return TaskResponseMapper.toUpdateTaskResponse(updateTask);
    }

    @Transactional
    @Override
    public UpdateTaskResponse updateTaskLabel(Long taskId, Long userId, UpdateTaskLabelRequest request) {
        memberService.findActiveMember(userId);
        memberService.findReviewer(userId);
        Task task = taskService.findById(taskId);
        Label label = labelService.findById(request.labelId());

        task.updateLabel(label);
        Task updatetask = taskService.upsert(task);
        return TaskResponseMapper.toUpdateTaskResponse(updatetask);
    }

    public void updateAgitPostId(ResponseEntity<String> responseEntity, Task task) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            task.updateAgitPostId(jsonNode.get("id").asLong());
            taskService.upsert(task);
        } catch (JsonProcessingException e) {
            throw new ApplicationException(NotificationErrorCode.AGIT_SEND_FAILED);
        }
    }

    private void updateAttachments(List<Long> attachmentIdsToDelete, List<MultipartFile> files, Task task) {
        List<Attachment> attachmentsToDelete = validateAndGetAttachments(attachmentIdsToDelete, task);
        attachmentsToDelete.forEach(Attachment::softDelete);

        if (files != null) {
            List<String> fileUrls = s3UploadPort.uploadFiles(FilePathPolicy.TASK_IMAGE, files);
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

    private void publishNotification(Task task, NotificationType notificationType, String message) {
        List<Member> receivers = List.of(task.getRequester(), task.getProcessor());
        receivers.forEach(receiver -> {
            sendNotificationService.sendPushNotification(receiver, notificationType,
                    task, message, null);
        });

            sendNotificationService.sendAgitNotification(notificationType, task, message, null);
    }
}
