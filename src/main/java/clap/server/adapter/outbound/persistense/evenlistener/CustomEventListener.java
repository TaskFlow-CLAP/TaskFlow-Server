package clap.server.adapter.outbound.persistense.evenlistener;

import clap.server.application.service.notification.CreateNotificationService;
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handler(Notification request) {
        createNotificationService.createNotification(request);
    }
}
