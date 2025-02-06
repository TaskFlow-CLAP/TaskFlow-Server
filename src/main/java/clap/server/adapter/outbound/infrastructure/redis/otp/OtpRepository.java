package clap.server.adapter.outbound.infrastructure.redis.otp;

import org.springframework.data.repository.CrudRepository;

public interface OtpRepository extends CrudRepository<OtpEntity, String>{
}
