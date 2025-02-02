package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import clap.server.adapter.outbound.persistense.mapper.NotificationPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.notification.NotificationRepository;
import clap.server.application.mapper.NotificationMapper;
import clap.server.application.port.outbound.notification.CommandNotificationPort;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements LoadNotificationPort, CommandNotificationPort {

    private final NotificationRepository notificationRepository;
    private final NotificationPersistenceMapper notificationPersistenceMapper;


    @Override
    public Optional<Notification> findById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notificationPersistenceMapper::toDomain);
    }

    @Override
    public SliceResponse<FindNotificationListResponse> findAllByReceiverId(Long receiverId, Pageable pageable) {
        Slice<Notification> notificationList = notificationRepository
                .findAllByReceiver_MemberIdOrderByCreatedAtDesc(receiverId, pageable)
                .map(notificationPersistenceMapper::toDomain);

        return NotificationMapper.toSliceOfFindNoticeListResponse(
                notificationList.map(NotificationMapper::toFindNoticeListResponse)
        );
    }

    @Override
    public List<Notification> findNotificationsByMemberId(Long memberId) {
        return notificationRepository.findAllByReceiver_MemberId(memberId)
                .stream().map(notificationPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countNotification(Long memberId) {
        return notificationRepository.countByIsReadFalseAndReceiver_MemberId(memberId);
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notificationPersistenceMapper.toEntity(notification));
    }
}
