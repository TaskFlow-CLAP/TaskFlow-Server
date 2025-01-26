package clap.server.application.port.inbound.member;

public interface ResetPasswordUsecase {
    void resetPassword(Long memberId, String password);
    void resetPasswordAndActivateMember(Long memberId, String password);
}
