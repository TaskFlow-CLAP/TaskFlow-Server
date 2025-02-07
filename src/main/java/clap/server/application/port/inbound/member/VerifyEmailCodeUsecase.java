package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.request.VerifyCodeRequest;

public interface VerifyEmailCodeUsecase {
    void verifyEmailCode(VerifyCodeRequest request);
}
