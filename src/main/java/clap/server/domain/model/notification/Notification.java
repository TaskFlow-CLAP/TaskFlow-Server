package clap.server.domain.model.notification;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTime {
    private Long notificationId;
    private Task task;
    private NotificationType type;
    private Member receiver;
    private String message;
    private String taskTitle;
    private boolean isRead;
}
