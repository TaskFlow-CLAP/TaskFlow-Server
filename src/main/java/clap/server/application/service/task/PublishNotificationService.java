package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.notification.SseRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.service.notification.SendWebhookService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

import static clap.server.domain.model.notification.Notification.createTaskNotification;

@Service
@RequiredArgsConstructor
public class PublishNotificationService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SendWebhookService sendWebhookService;

    public void publishNotification(List<Member> receivers, Task task, NotificationType type) {
        receivers.forEach(receiver -> {
            Notification notification = createTaskNotification(task, receiver, type);
            applicationEventPublisher.publishEvent(notification);

            publishSSENotification(task.getProcessor().getNickname(), receiver, notification);

            sendWebhookService.sendWebhookNotification(receiver, type,
                    task, task.getProcessor().getNickname(), null);
        });

    }

    public void publishCommentNotification(Member receiver, Task task, String message, String commenterName) {
        Notification notification = createTaskNotification(task, receiver, NotificationType.COMMENT);
        applicationEventPublisher.publishEvent(notification);

        publishSSENotification(message, receiver, notification);

        sendWebhookService.sendWebhookNotification(receiver, NotificationType.COMMENT, task, message, commenterName);
    }

    public void publishTaskUpdatedNotification(List<Member> receivers, Task task, NotificationType notificationType, String message) {
        receivers.forEach(receiver -> {
            Notification notification = createTaskNotification(task, receiver, notificationType);
            applicationEventPublisher.publishEvent(notification);

            publishSSENotification(message, receiver, notification);

            sendWebhookService.sendWebhookNotification(receiver, notificationType,
                    task, message, null);
        });
    }

    private void publishSSENotification(String message, Member receiver, Notification notification) {
        SseRequest sseRequest = new SseRequest(
                notification.getTask().getTitle(),
                notification.getType(),
                receiver.getMemberId(),
                message
        );
        applicationEventPublisher.publishEvent(sseRequest);
    }
}
