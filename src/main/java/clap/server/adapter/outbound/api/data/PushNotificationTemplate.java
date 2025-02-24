package clap.server.adapter.outbound.api.data;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;

public record PushNotificationTemplate(

        String email,
        NotificationType notificationType,
        String taskName,
        String senderName,
        String message,
        String commenterName,
        String reason
) {
}
