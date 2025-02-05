package clap.server.domain.model.notification;

import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTime {
    private Long notificationId;
    private Task task;
    private NotificationType type;
    private Member receiver;
    private String message;
    private boolean isRead;

    @Builder
    public Notification(Task task, NotificationType type, Member receiver, String message) {
        this.task = task;
        this.type = type;
        this.receiver = receiver;
        this.message = message;
        this.isRead = false;
    }

    public void updateNotificationIsRead() {
        this.isRead = true;
    }

    public static Notification createTaskNotification(Task task, Member reviewer, NotificationType type, String message) {
        return Notification.builder()
                .task(task)
                .type(type)
                .receiver(reviewer)
                .message(message)
                .build();
    }
}
