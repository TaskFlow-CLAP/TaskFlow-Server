package clap.server.application.port.inbound.member;

public interface VerifyPasswordUseCase {
    void verifyPassword(Long memberId, String password);
}
