package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.response.SendVerificationCodeRequest;

public interface SendVerificationEmailUsecase {
    void sendVerificationCode(SendVerificationCodeRequest request);
}
