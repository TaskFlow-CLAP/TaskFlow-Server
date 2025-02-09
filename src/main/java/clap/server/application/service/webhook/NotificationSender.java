package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;

public interface NotificationSender {
    void send(PushNotificationTemplate template, String taskDetailUrl);
}