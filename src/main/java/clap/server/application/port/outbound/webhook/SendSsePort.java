package clap.server.application.port.outbound.webhook;

import clap.server.adapter.inbound.web.dto.notification.request.SseRequest;

public interface SendSsePort {

    void send(SseRequest request);
}
