package clap.server.application.service.notification;

import clap.server.application.port.inbound.notification.SubscribeSseUsecase;
import clap.server.application.port.outbound.notification.CommandSsePort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

@ApplicationService
@RequiredArgsConstructor
public class SubscribeSseService implements SubscribeSseUsecase {

    private final CommandSsePort commandSsePort;

    // SSE 연결 지속 시간
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    @Override
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        commandSsePort.save(memberId, emitter);

        // SSE가 작업을 완료하거나 종료되었을때 emitterRepository에서 해당 연결 값 삭제
        emitter.onCompletion(() -> commandSsePort.delete(memberId));
        emitter.onTimeout(() -> commandSsePort.delete(memberId));

        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(memberId))
                    .data("Sse 연결 성공. [memberId = " + memberId + "]"));
        } catch (IOException e) {
            throw new ApplicationException(NotificationErrorCode.SSE_SEND_FAILED);
        }
        return emitter;
    }


}
