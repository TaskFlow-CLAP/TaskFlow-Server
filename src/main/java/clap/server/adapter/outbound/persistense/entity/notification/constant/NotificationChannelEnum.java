package clap.server.adapter.outbound.persistense.entity.notification.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationChannelEnum {
    KAKAO_WORK,
    EMAIL,
    AGIT
}
