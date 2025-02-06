package clap.server.application.service.webhook;

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
    private final SendWebhookEmailService sendWebhookEmailService;
    private final SendKaKaoWorkService sendKaKaoWorkService;
    private final CommandNotificationPort commandNotificationPort;

    @Async("notificationExecutor")
    public void sendPushNotification(Member receiver, NotificationType notificationType,
                                        Task task, String message, String commenterName, Boolean isManager) {

        String email = receiver.getMemberInfo().getEmail();
        String taskTitle = task.getTitle();
        String requesterNickname = task.getRequester().getNickname();

        String taskDetailUrl = extractTaskUrl(notificationType, task, isManager);

        Notification notification = createTaskNotification(task, receiver, notificationType, message, taskTitle);

        PushNotificationTemplate pushNotificationTemplate = new PushNotificationTemplate(
                email, notificationType, taskTitle, requesterNickname, message, commenterName
        );

        CompletableFuture<Void> saveNotification = CompletableFuture.runAsync(() -> {
            commandNotificationPort.save(notification);
        });

        CompletableFuture<Void> sendEmailFuture = CompletableFuture.runAsync(() -> {
            if (receiver.getEmailNotificationEnabled()) {
                sendWebhookEmailService.send(pushNotificationTemplate, taskDetailUrl);
            }
        });

        CompletableFuture<Void> sendKakaoWorkFuture = CompletableFuture.runAsync(() -> {
            if (receiver.getKakaoworkNotificationEnabled()) {
                sendKaKaoWorkService.send(pushNotificationTemplate, taskDetailUrl);
            }
        });

        //Todo : SSE 구현시 추가
        //SseRequest sseRequest = new SseRequest(
        //        taskTitle,
        //        notificationType,
        //        receiver.getMemberId(),
        //        message
        //);

        //Todo : SSE 구현시 추가
        //CompletableFuture<Void> sendSseFuture = CompletableFuture.runAsync(() -> {
        //    sendSsePort.send(sseRequest);
        //});

        CompletableFuture<Void> allOf = CompletableFuture.allOf(saveNotification,
                sendEmailFuture, sendKakaoWorkFuture);
        allOf.join();
    }

    @Async("notificationExecutor")
    public void sendAgitNotification(NotificationType notificationType,
                                     Task task, String message, String commenterName) {
        PushNotificationTemplate pushNotificationTemplate = new PushNotificationTemplate(
                null,
                notificationType,
                task.getTitle(),
                task.getRequester().getNickname(),
                message,
                commenterName
        );
        sendAgitService.sendAgit(pushNotificationTemplate, task);
    }
}
