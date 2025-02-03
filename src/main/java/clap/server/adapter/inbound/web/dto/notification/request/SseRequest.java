package clap.server.adapter.inbound.web.dto.notification.request;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;

public record SseRequest(
        String taskTitle,
        NotificationType notificationType,
        Long receiverId,
        String message
) {
}
