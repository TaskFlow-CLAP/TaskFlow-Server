package clap.server.adapter.inbound.web.dto.webhook;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;

public record SendKakaoWorkRequest(

        String email,
        NotificationType notificationType,
        String taskName,
        String senderName,
        String message,
        String commenterName
) {
}
