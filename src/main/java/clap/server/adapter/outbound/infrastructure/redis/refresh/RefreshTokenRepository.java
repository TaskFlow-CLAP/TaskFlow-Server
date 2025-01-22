package clap.server.adapter.outbound.infrastructure.redis.refresh;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long>{
}
