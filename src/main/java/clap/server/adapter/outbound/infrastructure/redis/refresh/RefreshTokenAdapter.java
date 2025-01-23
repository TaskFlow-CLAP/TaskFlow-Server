package clap.server.adapter.outbound.infrastructure.redis.refresh;

import clap.server.application.port.outbound.auth.CommandRefreshTokenPort;
import clap.server.application.port.outbound.auth.LoadRefreshTokenPort;
import clap.server.domain.model.auth.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements CommandRefreshTokenPort, LoadRefreshTokenPort {
	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenMapper refreshTokenMapper;

	public void save(RefreshToken refreshToken) {
		RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
		refreshTokenRepository.save(refreshTokenEntity);
	}

	public void delete(RefreshToken refreshToken) {
		RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
		refreshTokenRepository.delete(refreshTokenEntity);
	}

	public Optional<RefreshToken> findByMemberId(Long memberId) {
		return refreshTokenRepository.findById(memberId).map(refreshTokenMapper::toDomain);
	}
}
