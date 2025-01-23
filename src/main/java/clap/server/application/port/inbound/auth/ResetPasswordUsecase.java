package clap.server.application.port.inbound.auth;

public interface ResetPasswordUsecase {
    void resetPassword(Long memberId, String password);
    void resetPasswordAndActivateMember(Long memberId, String password);
}
