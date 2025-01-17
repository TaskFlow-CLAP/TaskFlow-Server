package clap.server.adapter.outbound.persistense.entity.notification.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationPublisherType {
    SYSTEM,
    MEMBER
}
