package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.application.port.outbound.webhook.SendWebhookEmailPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendWebhookEmailService {

    private final SendWebhookEmailPort sendWebhookEmailPort;

    public void sendEmail(PushNotificationTemplate request) {
        sendWebhookEmailPort.sendWebhookEmail(request);
    }
}
