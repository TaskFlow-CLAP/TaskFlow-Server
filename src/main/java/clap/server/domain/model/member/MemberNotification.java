package clap.server.domain.model.member;

import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.notification.NotificationChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberNotification extends BaseTime {
    private Long memberNotificationId;
    private Boolean isEnabled;
    private Member member;
    private NotificationChannel notificationChannel;
 }
