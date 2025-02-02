package clap.server.application.port.inbound.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SubscribeSseUsecase {

    SseEmitter subscribe(Long memberId);
}
