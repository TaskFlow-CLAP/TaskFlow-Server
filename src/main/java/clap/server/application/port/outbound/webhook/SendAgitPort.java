package clap.server.application.port.outbound.webhook;

import clap.server.adapter.outbound.api.dto.SendAgitRequest;

public interface SendAgitPort {
    void sendAgit(SendAgitRequest request);
}
