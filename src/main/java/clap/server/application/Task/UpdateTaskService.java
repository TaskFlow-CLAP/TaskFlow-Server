package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.AttachmentRequest;
import clap.server.adapter.inbound.web.dto.task.CreateAndUpdateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;
import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;

import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;



@ApplicationService
@RequiredArgsConstructor
@Slf4j
public class UpdateTaskService implements UpdateTaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final TaskService taskService;
    private final CommandTaskPort commandTaskPort;
    private final CommandAttachmentPort commandAttachmentPort;


    @Override
    @Transactional
    public CreateAndUpdateTaskResponse updateTask(Long requesterId, UpdateTaskRequest updateTaskRequest) {
        Member member = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(updateTaskRequest.categoryId());
        Task task = taskService.findById(updateTaskRequest.taskId());

        Task updatedTask = TaskMapper.toUpdatedTask(task,member, category, updateTaskRequest.title(), updateTaskRequest.description());
        Task savedTask = commandTaskPort.save(updatedTask);

        List<Long> attachmentIds = AttachmentMapper.toAttachmentIds(updateTaskRequest.attachmentRequests());
        commandAttachmentPort.deleteByIds(attachmentIds);

        List<Attachment> attachments = AttachmentMapper.toUpdateAttachments(savedTask, updateTaskRequest.attachmentRequests());
        commandAttachmentPort.saveAll(attachments);
        return TaskMapper.toCreateAndUpdateTaskResponse(savedTask);
    }
}
