package clap.server.application.port.outbound.auth.refresh;

import clap.server.domain.model.auth.RefreshToken;

import java.util.Optional;

public interface LoadRefreshTokenPort {
    Optional<RefreshToken> findByMemberId(Long memberId);
}