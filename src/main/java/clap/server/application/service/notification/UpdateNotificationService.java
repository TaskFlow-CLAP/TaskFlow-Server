package clap.server.application.service.notification;

import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import clap.server.adapter.outbound.persistense.repository.notification.NotificationRepository;
import clap.server.application.port.inbound.notification.UpdateNotificationUsecase;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ApplicationService
@RequiredArgsConstructor
public class UpdateNotificationService implements UpdateNotificationUsecase {

    private final LoadNotificationPort loadNotificationPort;
    private final CommandNotificationPort commandNotificationPort;


    @Transactional
    @Override
    public void updateNotification(Long notificationId) {
        Notification notification = loadNotificationPort.findById(notificationId).get();
        notification.updateNotificationIsRead();
        commandNotificationPort.save(notification);
    }
}
