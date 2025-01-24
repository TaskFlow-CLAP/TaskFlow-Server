package clap.server.application.service.Task;

import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;
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
    public UpdateTaskResponse updateTask(Long requesterId, UpdateTaskRequest updateTaskRequest) {
        memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(updateTaskRequest.categoryId());
        Task task = taskService.findById(updateTaskRequest.taskId());
        //TODO: 작업이 요청 상태인 경우만 업데이트 가능
        task.updateTask(category, updateTaskRequest.title(), updateTaskRequest.description());
        Task updatedTask = commandTaskPort.save(task);

        List<Long> attachmentIds = AttachmentMapper.toAttachmentIds(updateTaskRequest.attachmentRequests());
        commandAttachmentPort.deleteByIds(attachmentIds);

        List<Attachment> attachments = Attachment.updateAttachments(updatedTask, updateTaskRequest.attachmentRequests());
        commandAttachmentPort.saveAll(attachments);
        return TaskMapper.toUpdateTaskResponse(updatedTask);
    }
}
