package clap.server.application.port.outbound.auth.forbidden;

import clap.server.domain.model.auth.ForbiddenToken;

public interface ForbiddenTokenPort {
    void save(ForbiddenToken forbiddenToken);
    boolean getIsForbidden(String accessToken);
}
