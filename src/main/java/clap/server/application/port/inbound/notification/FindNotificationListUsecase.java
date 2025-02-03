package clap.server.application.port.inbound.notification;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.notification.response.FindNotificationListResponse;
import org.springframework.data.domain.Pageable;

public interface FindNotificationListUsecase {
    SliceResponse<FindNotificationListResponse> findNotificationList(Long receiverId, Pageable pageable);
}
