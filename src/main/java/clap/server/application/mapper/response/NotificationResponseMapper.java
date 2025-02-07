package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.common.SliceResponse;
import clap.server.adapter.inbound.web.dto.notification.response.CountNotificationResponse;
import clap.server.adapter.inbound.web.dto.notification.response.FindNotificationListResponse;
import clap.server.domain.model.notification.Notification;
import org.springframework.data.domain.Slice;

public class NotificationResponseMapper {
    private NotificationResponseMapper() {throw new IllegalStateException("Utility class");}

    public static FindNotificationListResponse toFindNoticeListResponse(Notification notification) {
        return new FindNotificationListResponse(
                notification.getNotificationId(),
                notification.getTask().getTaskId(),
                notification.getType(),
                notification.getReceiver().getMemberId(),
                notification.getTask().getTitle(),
                notification.getMessage() != null ? notification.getMessage() : null,
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public static SliceResponse<FindNotificationListResponse> toSliceOfFindNoticeListResponse(Slice<FindNotificationListResponse> slice) {
        return new SliceResponse<>(
                slice.getContent(),
                slice.hasNext(),
                slice.isFirst(),
                slice.isLast()
        );
    }

    public static CountNotificationResponse toCountNotificationResponse(Long memberId, Integer count) {
        return new CountNotificationResponse(
                memberId,
                count
        );
    }
}
