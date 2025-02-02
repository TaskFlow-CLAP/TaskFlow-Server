package clap.server.application.port.inbound.member;

public interface ResetInitialPasswordUsecase {
    void resetPasswordAndActivateMember(Long memberId, String password);
}
