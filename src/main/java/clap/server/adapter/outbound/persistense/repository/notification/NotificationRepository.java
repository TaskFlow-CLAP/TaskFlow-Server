package clap.server.adapter.outbound.persistense.repository.notification;

import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    Page<NotificationEntity> findAllByReceiver_MemberId(Long receiverId, Pageable pageable);
}