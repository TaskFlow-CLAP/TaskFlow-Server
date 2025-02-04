package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;

public interface SendEmailPort {

    void sendWebhookEmail(PushNotificationTemplate request);

    void sendInvitationEmail(String memberEmail, String receiverName, String initialPassword);

}