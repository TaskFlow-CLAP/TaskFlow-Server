package clap.server.application;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.StatusEntity;
import clap.server.adapter.outbound.persistense.mapper.StatusPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.StatusRepository;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.TaskUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Status;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.application.mapper.AttachmentMapper.toAttachment;
import static clap.server.application.mapper.TaskMapper.toTask;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService implements TaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final CommandTaskPort commandTaskPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final StatusRepository statusRepository; //TODO: 계층화
    private final StatusPersistenceMapper statusPersistenceMapper;

    @Override
    @Transactional
    public CreateTaskResponse createTask(Long requesterId, CreateTaskRequest createTaskRequest) {
        Member member = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(createTaskRequest.categoryId());

        StatusEntity statusEntity = statusRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Status not found")); //TODO: 계층화
        
        Task task = toTask(member, category, createTaskRequest.title(), createTaskRequest.description(), statusPersistenceMapper.toDomain(statusEntity));
        Task savedTask = commandTaskPort.save(task);

        Attachment attachment = toAttachment(savedTask, createTaskRequest.fileUrl());
        commandAttachmentPort.save(attachment);

        return new CreateTaskResponse(savedTask.getTaskId(), savedTask.getCategory().getCategoryId(), savedTask.getTitle());
    }
}
