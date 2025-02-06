package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;

public interface NotificationSender {
    void send(PushNotificationTemplate template);
}