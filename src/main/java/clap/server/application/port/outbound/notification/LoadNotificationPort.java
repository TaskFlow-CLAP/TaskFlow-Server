package clap.server.application.port.outbound.notification;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import clap.server.domain.model.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface LoadNotificationPort {

    Optional<Notification> findById(Long notificationId);

    Page<FindNotificationListResponse> findAllByReceiverId(Long receiverId, Pageable pageable);

    List<Notification> findNotificationsByMemberId(Long memberId);
}