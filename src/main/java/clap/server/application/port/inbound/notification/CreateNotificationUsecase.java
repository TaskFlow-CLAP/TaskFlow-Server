package clap.server.application.port.inbound.notification;

import clap.server.adapter.inbound.web.dto.notification.CreateNotificationRequest;

public interface CreateNotificationUsecase {
    void createNotification(CreateNotificationRequest request);
}
