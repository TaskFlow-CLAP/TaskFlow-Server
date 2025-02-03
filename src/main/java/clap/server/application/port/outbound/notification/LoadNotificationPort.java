package clap.server.application.port.outbound.notification;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.notification.response.FindNotificationListResponse;
import clap.server.domain.model.notification.Notification;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


public interface LoadNotificationPort {

    Optional<Notification> findById(Long notificationId);

    SliceResponse<FindNotificationListResponse> findAllByReceiverId(Long receiverId, Pageable pageable);

    List<Notification> findNotificationsByMemberId(Long memberId);

    Integer countNotification(Long memberId);
}