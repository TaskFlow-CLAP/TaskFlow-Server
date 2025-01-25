package clap.server.application.port.inbound.notification;

import clap.server.domain.model.notification.Notification;

public interface CreateNotificationUsecase {
    void createNotification(Notification notification);
}
