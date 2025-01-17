package clap.server.domain.model.notification;

import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationChannel extends BaseTime {
    private Long notificationChannelId;
    private Member admin;
    private String code;
    private String name;

 }
