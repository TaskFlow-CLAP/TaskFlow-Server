package clap.server.application.port.outbound.notification;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LoadNotificationPort {
    Page<FindNotificationListResponse> findAllByReceiverId(Long receiverId, Pageable pageable);
}