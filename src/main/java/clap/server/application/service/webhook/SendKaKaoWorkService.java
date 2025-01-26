package clap.server.application.service.webhook;

import clap.server.adapter.inbound.web.dto.webhook.SendKakaoWorkRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;
import clap.server.adapter.outbound.persistense.KakaoWorkPersistenceAdapter;
import clap.server.application.port.outbound.webhook.SendKaKaoWorkPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendKaKaoWorkService {

    private final SendKaKaoWorkPort sendKaKaoWorkPort;

    public void sendKaKaoWork(SendKakaoWorkRequest request) {
        sendKaKaoWorkPort.sendKakaoWord(request);
    }
}
