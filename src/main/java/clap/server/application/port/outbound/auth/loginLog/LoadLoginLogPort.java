package clap.server.application.port.outbound.auth.loginLog;

import clap.server.domain.model.auth.LoginLog;

import java.util.Optional;

public interface LoadLoginLogPort {
    Optional<LoginLog> findByClientIp(String clientIp);
}
