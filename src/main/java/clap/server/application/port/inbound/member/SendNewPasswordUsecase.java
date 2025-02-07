package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.request.SendInitialPasswordRequest;

public interface SendNewPasswordUsecase {
    void sendInitialPassword(SendInitialPasswordRequest request);
}
