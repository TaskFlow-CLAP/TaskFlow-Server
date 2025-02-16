package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskLabelRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskRequest;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.LabelService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskLabelUsecase;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.application.port.outbound.s3.S3UploadPort;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.constants.FilePathConstants;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static clap.server.domain.policy.task.TaskPolicyConstants.TASK_MAX_FILE_COUNT;

@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class UpdateTaskContentService implements UpdateTaskLabelUsecase, UpdateTaskUsecase {
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final LabelService labelService;
    private final TaskService taskService;

    private final LoadAttachmentPort loadAttachmentPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final S3UploadPort s3UploadPort;

    @Override
    @Transactional
    public void updateTask(Long requesterId, Long taskId, UpdateTaskRequest request, List<MultipartFile> files) {
        memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(request.categoryId());
        Task task = taskService.findById(taskId);
        int attachmentCount = getAttachmentCount(request, files, task);

        if (!request.attachmentsToDelete().isEmpty()) {
            deleteAttachments(request, task);
        }
        if (files != null) {
            updateAttachments(files, task);
        }
        task.updateTask(requesterId, category, request.title(), request.description(), attachmentCount);
        taskService.upsert(task);
    }

    private void deleteAttachments(UpdateTaskRequest request, Task task) {
        List<Attachment> attachmentsToDelete = validateAndGetAttachments(request.attachmentsToDelete(), task);
        attachmentsToDelete.stream()
                .peek(Attachment::softDelete)
                .forEach(commandAttachmentPort::save);
    }

    private void updateAttachments(List<MultipartFile> files, Task task) {
        List<String> fileUrls = s3UploadPort.uploadFiles(FilePathConstants.TASK_FILE, files);
        List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
    }

    private static int getAttachmentCount(UpdateTaskRequest request, List<MultipartFile> files, Task task) {
        int attachmentToAdd = files == null ? 0 : files.size();
        int attachmentCount = task.getAttachmentCount() - request.attachmentsToDelete().size() + attachmentToAdd;
        if (attachmentCount > TASK_MAX_FILE_COUNT) {
            throw new ApplicationException(TaskErrorCode.FILE_COUNT_EXCEEDED);
        }
        return attachmentCount;
    }

    private List<Attachment> validateAndGetAttachments(List<Long> attachmentIdsToDelete, Task task) {
        List<Attachment> attachmentsOfTask = loadAttachmentPort.findAllByTaskIdAndAttachmentId(task.getTaskId(), attachmentIdsToDelete);
        if (attachmentsOfTask.size() != attachmentIdsToDelete.size()) {
            throw new ApplicationException(TaskErrorCode.TASK_ATTACHMENT_NOT_FOUND);
        }
        return attachmentsOfTask;
    }

    @Transactional
    @Override
    public void updateTaskLabel(Long taskId, Long memberId, UpdateTaskLabelRequest request) {
        memberService.findActiveMember(memberId);
        memberService.findReviewer(memberId);
        Task task = taskService.findById(taskId);
        Label label = request.labelId() != null ? labelService.findById(request.labelId()) : null;

        task.updateLabel(label);
        taskService.upsert(task);
    }

}
