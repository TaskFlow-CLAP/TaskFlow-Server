package clap.server.application.port.inbound.notification;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Deprecated
public interface SubscribeSseUsecase {

    @Deprecated
    SseEmitter subscribe(Long memberId);
}
