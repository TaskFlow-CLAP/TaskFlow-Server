package clap.server.application.port.inbound.notification;

import clap.server.adapter.inbound.web.dto.notification.request.SseRequest;

public interface SendSseUsecase {

    void send(SseRequest request);
}
