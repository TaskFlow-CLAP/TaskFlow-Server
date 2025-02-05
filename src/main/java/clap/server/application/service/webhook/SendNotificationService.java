package clap.server.application.service.webhook;

import clap.server.adapter.inbound.web.dto.notification.request.SseRequest;
import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.notification.Notification;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;
import static clap.server.domain.model.notification.Notification.createTaskNotification;

@ApplicationService
@RequiredArgsConstructor
public class SendNotificationService {

    private final SendSseService sendSseService;
    private final SendAgitService sendAgitService;
    private final SendEmailService sendEmailService;
    private final SendKaKaoWorkService sendKaKaoWorkService;
    private final CommandNotificationPort commandNotificationPort;

    @Async("notificationExecutor")
    public void sendPushNotification(Member receiver, String email, NotificationType notificationType,
                                        Task task, String taskTitle, String message, String commenterName) {
        String requesterNickname = task.getRequester().getNickname();

        Notification notification = createTaskNotification(task, receiver, notificationType, message, taskTitle);

        SseRequest sseRequest = new SseRequest(
                taskTitle,
                notificationType,
                receiver.getMemberId(),
                message
        );

        PushNotificationTemplate pushNotificationTemplate = new PushNotificationTemplate(
                email, notificationType, taskTitle, requesterNickname, message, commenterName
        );

        CompletableFuture<Void> saveNotification = CompletableFuture.runAsync(() -> {
            commandNotificationPort.save(notification);
        });

        CompletableFuture<Void> sendSseFuture = CompletableFuture.runAsync(() -> {
            sendSseService.send(sseRequest);
        });

        CompletableFuture<Void> sendEmailFuture = CompletableFuture.runAsync(() -> {
            if (receiver.getEmailNotificationEnabled()) {
                sendEmailService.sendEmail(pushNotificationTemplate);
            }
        });

        CompletableFuture<Void> sendKakaoWorkFuture = CompletableFuture.runAsync(() -> {
            if (receiver.getKakaoworkNotificationEnabled()) {
                sendKaKaoWorkService.sendKaKaoWork(pushNotificationTemplate);
            }
        });

        CompletableFuture<Void> allOf = CompletableFuture.allOf(saveNotification, sendSseFuture,
                sendEmailFuture, sendKakaoWorkFuture);
        allOf.join();
    }

    @Async("notificationExecutor")
    public void sendAgitNotification(NotificationType notificationType,
                                     Task task, String taskTitle, String message, String commenterName) {
        PushNotificationTemplate pushNotificationTemplate = new PushNotificationTemplate(
                null,
                notificationType,
                taskTitle,
                task.getRequester().getNickname(),
                message,
                commenterName
        );

        sendAgitService.sendAgit(pushNotificationTemplate, task);
    }
}
