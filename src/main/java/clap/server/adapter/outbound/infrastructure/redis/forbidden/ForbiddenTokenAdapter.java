package clap.server.adapter.outbound.infrastructure.redis.forbidden;

import clap.server.application.port.outbound.auth.ForbiddenTokenPort;
import clap.server.common.annotation.architecture.InfrastructureAdapter;
import clap.server.domain.model.auth.ForbiddenToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@InfrastructureAdapter
@RequiredArgsConstructor
public class ForbiddenTokenAdapter implements ForbiddenTokenPort {
    private final ForbiddenTokenRepository forbiddenTokenRepository;
    private final ForbiddenTokenMapper forbiddenTokenMapper;

    public void save(ForbiddenToken forbiddenToken) {
        forbiddenTokenRepository.save(forbiddenTokenMapper.toEntity(forbiddenToken));
    }

    public boolean getIsForbidden(String accessToken) {
        return forbiddenTokenRepository.existsById(accessToken);
    }
}
