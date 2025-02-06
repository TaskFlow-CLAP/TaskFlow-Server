package clap.server.adapter.outbound.infrastructure.sse;

import clap.server.adapter.inbound.web.dto.notification.request.SseRequest;
import clap.server.adapter.outbound.infrastructure.sse.repository.EmitterRepository;
import clap.server.application.port.outbound.webhook.SendSsePort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import clap.server.exception.AdapterException;
import clap.server.exception.code.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@InfrastructureAdapter
@RequiredArgsConstructor
public class SendSseService implements SendSsePort {

    private final EmitterRepository emitterRepository;

    @Override
    public void send(SseRequest request) {
        SseEmitter sseEmitter = emitterRepository.get(request.receiverId());
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(String.valueOf(request.receiverId()))
                    .data(request));
        } catch (Exception e) {
            throw new AdapterException(NotificationErrorCode.SSE_SEND_FAILED);
        }
    }
}
