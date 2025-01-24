package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.notification.FindNotificationListResponse;
import clap.server.domain.model.notification.Notification;

public class NotificationMapper {
    private NotificationMapper() {throw  new IllegalArgumentException();}

    public static FindNotificationListResponse toFindNoticeListResponse(Notification notification) {
        return new FindNotificationListResponse(
                notification.getNotificationId(),
                notification.getTask().getTaskId(),
                notification.getType(),
                notification.getReceiver().getMemberId(),
                notification.getTask().getTitle(),
                notification.getMessage() != null ? notification.getMessage() : null,
                notification.getCreatedAt()
        );
    }
}
