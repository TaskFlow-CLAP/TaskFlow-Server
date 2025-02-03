package clap.server.application.port.inbound.notification;

public interface UpdateNotificationUsecase {
    void updateNotification(Long userId, Long notificationId);
}
