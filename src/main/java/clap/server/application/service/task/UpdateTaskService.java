package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.TaskMapper;
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
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.constants.FilePathConstants;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.*;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class UpdateTaskService implements UpdateTaskUsecase, UpdateTaskStatusUsecase, UpdateTaskProcessorUsecase, UpdateTaskLabelUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TaskService taskService;

    private final CommandTaskPort commandTaskPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final LabelService labelService;
    private final CommandAttachmentPort commandAttachmentPort;
    private final S3UploadPort s3UploadPort;
    private final CommandTaskHistoryPort commandTaskHistoryPort;

    @Override
    @Transactional
    public UpdateTaskResponse updateTask(Long requesterId, Long taskId, UpdateTaskRequest updateTaskRequest, List<MultipartFile> files) {
        Member requester = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(updateTaskRequest.categoryId());
        Task task = taskService.findById(taskId);

        task.updateTask(requesterId, category, updateTaskRequest.title(), updateTaskRequest.description());
        Task updatedTask = commandTaskPort.save(task);

        if (!updateTaskRequest.attachmentsToDelete().isEmpty()) {
            updateAttachments(updateTaskRequest.attachmentsToDelete(), files, task);
        }
        return TaskMapper.toUpdateTaskResponse(updatedTask);
    }

    @Override
    @Transactional
    public UpdateTaskResponse updateTaskState(Long memberId, Long taskId, UpdateTaskStatusRequest updateTaskStatusRequest) {
        memberService.findActiveMember(memberId);
        Task task = taskService.findById(taskId);
        task.updateTaskStatus(updateTaskStatusRequest.taskStatus());
        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.STATUS_SWITCHED, task, task.getTaskStatus().getDescription(), null, null);
        commandTaskHistoryPort.save(taskHistory);
        return TaskMapper.toUpdateTaskResponse(commandTaskPort.save(task));

        // TODO : 알림 생성 로직 및 푸시 알림 로직 추가
    }

    @Transactional
    @Override
    public UpdateTaskResponse updateTaskProcessor(Long taskId, Long userId, UpdateTaskProcessorRequest request) {
        memberService.findReviewer(userId);
        Member processor = memberService.findById(request.processorId());
        Task task = taskService.findById(taskId);
        task.updateProcessor(processor);
        TaskHistory taskHistory = TaskHistory.createTaskHistory(TaskHistoryType.PROCESSOR_CHANGED, task, null, processor, null);
        commandTaskHistoryPort.save(taskHistory);
        return TaskMapper.toUpdateTaskResponse(commandTaskPort.save(task));

        // TODO : 알림 생성 로직 및 푸시 알림 로직 추가
    }

    @Transactional
    @Override
    public UpdateTaskResponse updateTaskLabel(Long taskId, Long userId, UpdateTaskLabelRequest request) {
        memberService.findReviewer(userId);
        Task task = taskService.findById(taskId);
        Label label = labelService.findById(request.labelId());
        task.updateLabel(label);
        return TaskMapper.toUpdateTaskResponse(commandTaskPort.save(task));
    }

    private void updateAttachments(List<Long> attachmentIdsToDelete, List<MultipartFile> files, Task task) {
        List<Attachment> attachmentsToDelete = validateAndGetAttachments(attachmentIdsToDelete, task);
        attachmentsToDelete.forEach(Attachment::softDelete);

        List<String> fileUrls = s3UploadPort.uploadFiles(FilePathConstants.TASK_IMAGE, files);
        List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
    }

    private List<Attachment> validateAndGetAttachments(List<Long> attachmentIdsToDelete, Task task) {
        List<Attachment> attachmentsOfTask = loadAttachmentPort.findAllByTaskIdAndCommentIsNullAndAttachmentId(task.getTaskId(), attachmentIdsToDelete);
        if (attachmentsOfTask.size() != attachmentIdsToDelete.size()) {
            throw new ApplicationException(TaskErrorCode.TASK_ATTACHMENT_NOT_FOUND);
        }
        return attachmentsOfTask;
    }
}
