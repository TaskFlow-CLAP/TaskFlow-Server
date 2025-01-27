package clap.server.adapter.outbound.infrastructure.sse.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {
    void save(Long id, SseEmitter emitter);

    void delete(Long id);

    SseEmitter get(Long id);

}
