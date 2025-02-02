package clap.server.application.port.inbound.notification;

import clap.server.adapter.inbound.web.dto.notification.SseRequest;

public interface SendSseUsecase {

    void send(SseRequest request);
}
