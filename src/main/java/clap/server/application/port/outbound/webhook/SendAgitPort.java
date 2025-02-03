package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;

public interface SendAgitPort {
    void sendAgit(PushNotificationTemplate request);
}
