package clap.server.application.service.notification;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.notification.response.FindNotificationListResponse;
import clap.server.application.mapper.response.NotificationResponseMapper;
import clap.server.application.port.inbound.notification.FindNotificationListUsecase;
import clap.server.application.port.outbound.notification.LoadNotificationPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindNotificationListService implements FindNotificationListUsecase {

    private final LoadNotificationPort loadNotificationPort;


    @Override
    public SliceResponse<FindNotificationListResponse> findNotificationList(Long receiverId, Pageable pageable) {
        return NotificationResponseMapper.toSliceOfFindNoticeListResponse(loadNotificationPort.findAllByReceiverId(receiverId, pageable)
                .map(NotificationResponseMapper::toFindNoticeListResponse)
        );
    }
}
