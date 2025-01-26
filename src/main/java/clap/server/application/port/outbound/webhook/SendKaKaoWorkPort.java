package clap.server.application.port.outbound.webhook;

import clap.server.adapter.inbound.web.dto.webhook.SendKakaoWorkRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;

public interface SendKaKaoWorkPort {

    void sendKakaoWord(SendKakaoWorkRequest request);
}
