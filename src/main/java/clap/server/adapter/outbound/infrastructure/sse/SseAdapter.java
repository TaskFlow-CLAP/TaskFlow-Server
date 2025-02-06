package clap.server.adapter.outbound.infrastructure.sse;

import clap.server.adapter.outbound.infrastructure.sse.repository.EmitterRepository;
import clap.server.application.port.outbound.notification.CommandSsePort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@InfrastructureAdapter
@RequiredArgsConstructor
public class SseAdapter implements CommandSsePort {

    private final EmitterRepository emitterRepository;

    @Override
    public void save(Long receiverId, SseEmitter emitter) {
        emitterRepository.save(receiverId, emitter);
    }

    @Override
    public void delete(Long receiverId) {
        emitterRepository.delete(receiverId);
    }
}
