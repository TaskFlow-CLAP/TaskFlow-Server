package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.application.port.outbound.webhook.SendEmailPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendEmailService {

    private final SendEmailPort port;

    public void sendEmail(PushNotificationTemplate request) {
        port.sendWebhookEmail(request);
    }
}
