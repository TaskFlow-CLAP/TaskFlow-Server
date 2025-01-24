package clap.server.application.port.inbound.notification;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindNotificationListUsecase {
    Page<FindNotificationListResponse> findNotificationList(Long receiverId, Pageable pageable);
}
