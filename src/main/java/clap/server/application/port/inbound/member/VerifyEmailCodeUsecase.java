package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.response.VerifyCodeRequest;

public interface VerifyEmailCodeUsecase {
    void verifyEmailCode(VerifyCodeRequest request);
}
