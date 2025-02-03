package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
import clap.server.application.port.outbound.webhook.SendKaKaoWorkPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendKaKaoWorkService {

    private final SendKaKaoWorkPort sendKaKaoWorkPort;

    public void sendKaKaoWork(SendWebhookRequest request) {
        sendKaKaoWorkPort.sendKakaoWork(request);
    }
}
