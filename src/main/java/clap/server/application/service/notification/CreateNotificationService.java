package clap.server.application.service.notification;

import clap.server.adapter.inbound.web.dto.notification.CreateNotificationRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.domain.TaskService;
import clap.server.application.port.inbound.notification.CreateNotificationUsecase;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class CreateNotificationService implements CreateNotificationUsecase {

    private final MemberService memberService;
    private final TaskService taskService;
    private final CommandNotificationPort commandNotificationPort;

    @Override
    public void createNotification(CreateNotificationRequest request) {
        Member receiver = memberService.findById(request.receiverId());
        Task task = taskService.findById(request.taskId());

        Notification notification = Notification.builder()
                        .task(task)
                        .type(request.notificationType())
                        .receiver(receiver)
                        .message(request.message())
                        .build();

        commandNotificationPort.save(notification);
    }
}
