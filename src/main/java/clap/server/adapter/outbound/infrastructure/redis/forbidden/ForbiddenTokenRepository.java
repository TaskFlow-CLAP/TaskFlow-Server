package clap.server.adapter.outbound.infrastructure.redis.forbidden;

import org.springframework.data.repository.CrudRepository;

public interface ForbiddenTokenRepository extends CrudRepository<ForbiddenTokenEntity, String> {
}