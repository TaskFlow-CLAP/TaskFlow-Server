package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;

public interface SendWebhookEmailPort {

    void sendWebhookEmail(PushNotificationTemplate request, String taskDetailUrl);
}