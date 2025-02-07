package clap.server.application.port.outbound.notification;

import clap.server.domain.model.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;


public interface LoadNotificationPort {

    Optional<Notification> findById(Long notificationId);

    Slice<Notification> findAllByReceiverId(Long receiverId, Pageable pageable);

    List<Notification> findNotificationsByMemberId(Long memberId);

    Integer countNotification(Long memberId);
}