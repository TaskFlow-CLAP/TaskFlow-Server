package clap.server.application.Task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.inbound.domain.CategoryService;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.outbound.task.CommandAttachmentPort;
import clap.server.application.port.outbound.task.CommandTaskPort;

import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@ApplicationService
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUsecase {

    private final MemberService memberService;
    private final CategoryService categoryService;
    private final CommandTaskPort commandTaskPort;
    private final CommandAttachmentPort commandAttachmentPort;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public CreateTaskResponse createTask(Long requesterId, CreateTaskRequest createTaskRequest) {
        Member member = memberService.findActiveMember(requesterId);
        Category category = categoryService.findById(createTaskRequest.categoryId());
        Task task = Task.createTask(member, category, createTaskRequest.title(), createTaskRequest.description());
        Task savedTask = commandTaskPort.save(task);

        List<Attachment> attachments = Attachment.createAttachments(savedTask, createTaskRequest.fileUrls());
        commandAttachmentPort.saveAll(attachments);


        // requestDto에 알림 데이터 mapping

        List<Member> reviewers = memberService.findReviewers();


        // 검토자들 각각에 대한 알림 생성 후 event 발행
        for (Member reviewer : reviewers) {
            Notification notification = Notification.builder()
                    .task(savedTask)
                    .type(NotificationType.TASK_REQUESTED)
                    .receiver(reviewer)
                    .message(null)
                    .build();
            // publish event로 event 발행
            applicationEventPublisher.publishEvent(notification);
        }

        return TaskMapper.toCreateTaskResponse(savedTask);
    }
}
