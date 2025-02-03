package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;

public interface SendAgitPort {
    void sendAgit(SendWebhookRequest request);
}
