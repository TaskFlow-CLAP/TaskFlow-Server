package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.request.SendVerificationCodeRequest;

public interface SendVerificationEmailUsecase {
    void sendVerificationCode(SendVerificationCodeRequest request);
}
