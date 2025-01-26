package clap.server.adapter.outbound.persistense.evenlistener;

import clap.server.adapter.inbound.web.dto.notification.SseRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendAgitRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendKakaoWorkRequest;
import clap.server.adapter.inbound.web.dto.webhook.SendWebhookRequest;
import clap.server.application.service.notification.CreateNotificationService;
import clap.server.application.service.notification.SendSseService;
import clap.server.application.service.webhook.SendAgitService;
import clap.server.application.service.webhook.SendEmailService;
import clap.server.application.service.webhook.SendKaKaoWorkService;
import clap.server.domain.model.notification.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// 이벤트 발행 시 실행
@RequiredArgsConstructor
@Component
public class CustomEventListener {
    private final CreateNotificationService createNotificationService;
    private final SendSseService sendSseService;
    private final SendEmailService sendEmailService;
    private final SendKaKaoWorkService sendKaKaoWorkService;
    private final SendAgitService agitService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handler(Notification request) {
        createNotificationService.createNotification(request);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSseNotification(SseRequest sseRequest) {
        sendSseService.send(sseRequest);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailSend(SendWebhookRequest email) {
        sendEmailService.sendEmail(email);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleKakaoWorkSend(SendKakaoWorkRequest kakaoWork) {
        sendKaKaoWorkService.sendKaKaoWork(kakaoWork);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAgitSend(SendAgitRequest agit) {
        agitService.sendAgit(agit);
    }
}
