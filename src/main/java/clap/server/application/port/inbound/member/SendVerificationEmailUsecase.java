package clap.server.application.port.inbound.member;

public interface SendVerificationEmailUsecase {
    void sendVerificationCode(Long memberId);
}
