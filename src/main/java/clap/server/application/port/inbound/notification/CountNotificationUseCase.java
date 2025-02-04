package clap.server.application.port.inbound.notification;

import clap.server.adapter.inbound.web.dto.notification.response.CountNotificationResponse;

public interface CountNotificationUseCase {

    CountNotificationResponse countNotification(Long userId);
}
