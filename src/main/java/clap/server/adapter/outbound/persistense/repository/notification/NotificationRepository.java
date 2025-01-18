package clap.server.adapter.outbound.persistense.repository.notification;

import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}