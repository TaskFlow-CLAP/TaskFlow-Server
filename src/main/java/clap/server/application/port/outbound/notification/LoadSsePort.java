package clap.server.application.port.outbound.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface LoadSsePort {

    SseEmitter get(Long receiverId);
}
