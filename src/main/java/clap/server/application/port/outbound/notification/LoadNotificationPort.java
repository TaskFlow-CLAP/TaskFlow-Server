package clap.server.application.port.outbound.notification;

import clap.server.domain.model.notification.Notification;
import java.util.Optional;

public interface LoadNotificationPort {
    Optional<Notification> findById(Long id);
}