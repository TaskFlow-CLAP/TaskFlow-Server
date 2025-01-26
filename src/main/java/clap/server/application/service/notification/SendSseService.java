package clap.server.application.service.notification;

import clap.server.adapter.inbound.web.dto.notification.SseRequest;
import clap.server.application.port.inbound.notification.SendSseUsecase;
import clap.server.application.port.outbound.notification.LoadSsePort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.NotificationErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@ApplicationService
@RequiredArgsConstructor
public class SendSseService implements SendSseUsecase {

    private final LoadSsePort loadSsePort;

    @Override
    public void send(SseRequest request) {
        SseEmitter sseEmitter = loadSsePort.get(request.receiverId());
        try {
            sseEmitter.send(SseEmitter.event()
                    .id(String.valueOf(request.receiverId()))
                    .data(request));
        } catch (IOException e) {
            throw new ApplicationException(NotificationErrorCode.SSE_SEND_FAILED);
        }
    }
}
