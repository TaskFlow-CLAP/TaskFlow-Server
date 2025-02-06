package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.dto.PushNotificationTemplate;
import clap.server.application.port.outbound.webhook.SendWebhookEmailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendWebhookEmailService implements NotificationSender {

    private final SendWebhookEmailPort sendWebhookEmailPort;

    public void send(PushNotificationTemplate request, String taskDetailUrl) {
        sendWebhookEmailPort.sendWebhookEmail(request, taskDetailUrl);
    }
}
