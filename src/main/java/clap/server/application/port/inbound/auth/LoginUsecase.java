package clap.server.application.port.inbound.auth;

import clap.server.adapter.inbound.web.dto.auth.response.LoginResponse;

public interface LoginUsecase {
    LoginResponse login(String nickname, String password, String clientIp);
}
