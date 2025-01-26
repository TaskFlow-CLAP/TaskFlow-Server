package clap.server.application.port.outbound.webhook;

import clap.server.adapter.inbound.web.dto.webhook.SendAgitRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;

public interface SendAgitPort {
    void sendAgit(SendAgitRequest request);
}
