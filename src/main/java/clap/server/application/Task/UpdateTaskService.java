package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;
import clap.server.adapter.outbound.infrastructure.s3.S3UploadAdapter;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadAttachmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.FilePath;
import clap.server.domain.model.task.Task;

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
public class UpdateTaskService implements UpdateTaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TaskService taskService;
    private final CommandTaskPort commandTaskPort;
    private final LoadAttachmentPort loadAttachmentPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final S3UploadAdapter s3UploadAdapter;

    @Override
    @Transactional
    public UpdateTaskResponse updateTask(Long requesterId, Long taskId, UpdateTaskRequest updateTaskRequest, List<MultipartFile> files) {
        memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(updateTaskRequest.categoryId());
        Task task = taskService.findById(taskId);
        if(task.getTaskStatus() != TaskStatus.REQUESTED){
            throw new ApplicationException(TaskErrorCode.TASK_STATUS_MISMATCH);
        }
        task.updateTask(category, updateTaskRequest.title(), updateTaskRequest.description());
        Task updatedTask = commandTaskPort.save(task);

        if (!updateTaskRequest.attachmentsToDelete().isEmpty()){
            updateAttachments(updateTaskRequest.attachmentsToDelete(), files, task);
        }
        return TaskMapper.toUpdateTaskResponse(updatedTask);
    }

    private void updateAttachments(List<Long> attachmentIdsToDelete, List<MultipartFile> files, Task task) {
        validateAttachments(attachmentIdsToDelete, task);
        commandAttachmentPort.deleteByIds(attachmentIdsToDelete);

        List<String> fileUrls = s3UploadAdapter.uploadFiles(FilePath.TASK_IMAGE, files);
        List<Attachment> attachments = AttachmentMapper.toTaskAttachments(task, files, fileUrls);
        commandAttachmentPort.saveAll(attachments);
    }

    private void validateAttachments(List<Long> attachmentIdsToDelete, Task task) {
        List<Attachment> attachmentsOfTask = loadAttachmentPort.findAllByTaskIdAndCommentIsNullAndAttachmentId(task.getTaskId(), attachmentIdsToDelete);
        if(attachmentsOfTask.size() != attachmentIdsToDelete.size()) {
            throw new ApplicationException(TaskErrorCode.TASK_ATTACHMENT_NOT_FOUND);
        }
    }
}
