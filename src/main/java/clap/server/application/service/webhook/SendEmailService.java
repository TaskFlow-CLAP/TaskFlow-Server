package clap.server.application.service.webhook;

import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;
import clap.server.application.port.outbound.webhook.SendEmailPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class SendEmailService {

    private final SendEmailPort port;

    public void sendEmail(SendWebhookRequest request) {
        port.sendEmail(request);
    }
}
