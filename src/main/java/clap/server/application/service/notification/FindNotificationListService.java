package clap.server.application.service.notification;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import clap.server.application.port.inbound.notification.FindNotificationListUsecase;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindNotificationListService implements FindNotificationListUsecase {

    private final LoadNotificationPort loadNotificationPort;


    @Override
    public Page<FindNotificationListResponse> findNotificationList(Long receiverId, Pageable pageable) {
        return loadNotificationPort.findAllByReceiverId(receiverId, pageable);
    }
}
