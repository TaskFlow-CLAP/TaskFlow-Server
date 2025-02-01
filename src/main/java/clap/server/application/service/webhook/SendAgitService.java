package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
import clap.server.application.port.outbound.webhook.SendAgitPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendAgitService {

    private final SendAgitPort agitPort;

    public void sendAgit(SendWebhookRequest request) {
        agitPort.sendAgit(request);
    }
}
