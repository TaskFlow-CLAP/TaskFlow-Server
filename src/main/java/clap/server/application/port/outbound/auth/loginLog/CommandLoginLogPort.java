package clap.server.application.port.outbound.auth.loginLog;

import clap.server.domain.model.auth.LoginLog;

public interface CommandLoginLogPort {
    void save(LoginLog loginLog);

    void deleteById(String clientIp);
}
