package clap.server.adapter.outbound.api.dto;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;

public record PushNotificationTemplate(

        Long taskId,
        String email,
        NotificationType notificationType,
        String taskName,
        String senderName,
        String message,
        String commenterName
) {
}
