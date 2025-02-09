package clap.server.application.service.webhook;

import clap.server.adapter.outbound.api.data.PushNotificationTemplate;
import clap.server.application.port.outbound.webhook.SendKaKaoWorkPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendKaKaoWorkService implements NotificationSender {

    private final SendKaKaoWorkPort sendKaKaoWorkPort;

    public void send(PushNotificationTemplate request, String taskDetailUrl) {
        sendKaKaoWorkPort.sendKakaoWork(request, taskDetailUrl);
    }
}
