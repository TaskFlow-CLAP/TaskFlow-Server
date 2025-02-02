package clap.server.application.port.outbound.auth;

import clap.server.domain.model.auth.ForbiddenToken;

public interface ForbiddenTokenPort {
    void save(ForbiddenToken forbiddenToken);
    boolean getIsForbidden(String accessToken);
}
