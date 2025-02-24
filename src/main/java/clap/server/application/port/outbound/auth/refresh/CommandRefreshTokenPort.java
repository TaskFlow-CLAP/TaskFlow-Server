package clap.server.application.port.outbound.auth.refresh;

import clap.server.domain.model.auth.RefreshToken;

public interface CommandRefreshTokenPort {
    void save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);
}