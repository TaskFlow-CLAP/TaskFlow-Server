package clap.server.application.port.outbound.notification;


import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface CommandSsePort {

    void save(Long receiverId, SseEmitter emitter);

    void delete(Long receiverId);

}
