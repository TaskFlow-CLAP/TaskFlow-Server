package clap.server.application;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;

import clap.server.application.mapper.AttachmentMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;

import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static clap.server.application.mapper.TaskMapper.toCreateTaskResponse;
import static clap.server.application.mapper.TaskMapper.toTask;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateTaskService implements CreateTaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final CommandTaskPort commandTaskPort;
    private final CommandAttachmentPort commandAttachmentPort;

    @Override
    @Transactional
    public CreateTaskResponse createTask(Long requesterId, CreateTaskRequest createTaskRequest) {
        Member member = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(createTaskRequest.categoryId());

        Task task = toTask(member, category, createTaskRequest.title(), createTaskRequest.description());
        Task savedTask = commandTaskPort.save(task);

        List<Attachment> attachments = AttachmentMapper.toAttachments(savedTask, createTaskRequest.fileUrls());
        commandAttachmentPort.saveAll(attachments);

        return toCreateTaskResponse(savedTask);
    }
}