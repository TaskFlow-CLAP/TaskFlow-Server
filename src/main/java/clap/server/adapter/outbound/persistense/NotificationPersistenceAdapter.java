package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import clap.server.adapter.outbound.persistense.mapper.NotificationPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.notification.NotificationRepository;
import clap.server.application.mapper.NotificationMapper;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@PersistenceAdapter
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements LoadNotificationPort {

    private final NotificationRepository notificationRepository;
    private final NotificationPersistenceMapper notificationPersistenceMapper;


    @Override
    public Page<FindNotificationListResponse> findAllByReceiverId(Long receiverId, Pageable pageable) {
        Page<Notification> notificationList = notificationRepository.findAllByReceiver_MemberId(receiverId, pageable)
                .map(notificationPersistenceMapper::toDomain);
        return notificationList.map(NotificationMapper::toFindNoticeListResponse);
    }
}
