package clap.server.adapter.outbound.persistense.repository.notification;

import clap.server.adapter.outbound.persistense.entity.notification.NotificationChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationChannelRepository extends JpaRepository<NotificationChannelEntity, Long> {
}