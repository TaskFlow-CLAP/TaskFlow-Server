package clap.server.application.port.outbound.webhook;

import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;

public interface SendEmailPort {

    void sendEmail(SendWebhookRequest request);
}