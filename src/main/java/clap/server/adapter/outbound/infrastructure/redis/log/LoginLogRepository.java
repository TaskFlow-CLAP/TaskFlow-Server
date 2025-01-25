package clap.server.adapter.outbound.infrastructure.redis.log;

import org.springframework.data.repository.CrudRepository;

public interface LoginLogRepository  extends CrudRepository<LoginLogEntity, String> {
}
