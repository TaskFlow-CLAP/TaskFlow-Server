package clap.server.application.service.webhook;

import clap.server.adapter.inbound.web.dto.notification.request.SseRequest;
import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
import clap.server.application.port.outbound.webhook.SendSsePort;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendSseService {
    private final SendSsePort sendSsePort;

    public void send(Member receiver, NotificationType notificationType,
                     Task task, String message) {
        SseRequest sseRequest = new SseRequest(
                task.getTitle(),
                notificationType,
                receiver.getMemberId(),
                message
        );
        sendSsePort.send(sseRequest);
    }
}