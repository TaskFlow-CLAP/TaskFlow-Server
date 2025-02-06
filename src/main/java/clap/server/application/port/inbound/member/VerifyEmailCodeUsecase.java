package clap.server.application.port.inbound.member;

public interface VerifyEmailCodeUsecase {
    void verifyEmailCode(Long memberId, String code);
}
