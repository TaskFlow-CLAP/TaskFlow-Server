package clap.server.config.evenlistener;

import clap.server.adapter.inbound.web.dto.notification.CreateNotificationRequest;
import clap.server.application.service.notification.CreateNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// 이벤트 발행 시 실행
@RequiredArgsConstructor
@Component
public class CustomEventListener {
    private final CreateNotificationService createNotificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handler(CreateNotificationRequest request) {
        createNotificationService.createNotification(request);
    }
}
