package clap.server.adapter.outbound.persistense.repository.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    void save(Long id, SseEmitter emitter);

    void delete(Long id);

    SseEmitter get(Long id);

}
