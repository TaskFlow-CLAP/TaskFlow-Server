package clap.server.application.port.inbound.auth;

import clap.server.adapter.inbound.web.dto.auth.LoginResponse;

public interface AuthUsecase {
    LoginResponse login(String nickname, String password, String sessionId, String clientIp);
    void logout(Long memberId, String accessToken, String refreshToken);
}
