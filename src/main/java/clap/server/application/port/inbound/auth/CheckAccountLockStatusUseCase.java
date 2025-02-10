package clap.server.application.port.inbound.auth;

public interface CheckAccountLockStatusUseCase {
    void checkAccountIsLocked(String nickname);
}
