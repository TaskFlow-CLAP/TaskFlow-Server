package clap.server.adapter.outbound.infrastructure.redis.log;

import clap.server.application.port.outbound.auth.CommandLoginLogPort;
import clap.server.application.port.outbound.auth.LoadLoginLogPort;
import clap.server.domain.model.auth.LoginLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginLogAdapter implements LoadLoginLogPort, CommandLoginLogPort {
	private final LoginLogRepository loginLogRepository;
	private final LoginLogMapper loginLogMapper;

	public void save(LoginLog loginLog) {
		LoginLogEntity loginLogEntity = loginLogMapper.toEntity(loginLog);
		loginLogRepository.save(loginLogEntity);
	}

	public void deleteById(String sessionId) {
		loginLogRepository.deleteById(sessionId);
	}

	public Optional<LoginLog> findBySessionId(String sessionId) {
		return loginLogRepository.findById(sessionId).map(loginLogMapper::toDomain);
	}
}
