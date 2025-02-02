package clap.server.application.port.inbound.auth;

public interface LogoutUsecase {
    void logout(Long memberId, String accessToken, String refreshToken);
}
