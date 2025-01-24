package clap.server.adapter.inbound.web.dto.notification;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import jakarta.validation.constraints.NotNull;

public record CreateNotificationRequest(
        @NotNull
        Long taskId,
        @NotNull
        NotificationType notificationType,
        @NotNull
        Long receiverId,
        String message
) {
}
