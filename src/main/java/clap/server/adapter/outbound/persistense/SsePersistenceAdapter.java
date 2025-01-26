package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.repository.notification.EmitterRepository;
import clap.server.application.port.outbound.notification.CommandSsePort;
import clap.server.application.port.outbound.notification.LoadSsePort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@PersistenceAdapter
@RequiredArgsConstructor
public class SsePersistenceAdapter implements LoadSsePort, CommandSsePort {

    private final EmitterRepository emitterRepository;

    @Override
    public void save(Long receiverId, SseEmitter emitter) {
        emitterRepository.save(receiverId, emitter);
    }

    @Override
    public void delete(Long receiverId) {
        emitterRepository.delete(receiverId);
    }

    @Override
    public SseEmitter get(Long receiverId) {
        return emitterRepository.get(receiverId);
    }
}
