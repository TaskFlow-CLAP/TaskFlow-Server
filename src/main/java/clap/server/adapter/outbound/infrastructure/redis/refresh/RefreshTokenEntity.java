package clap.server.adapter.outbound.infrastructure.redis.refresh;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("refreshToken")
@Builder
@ToString(of = {"memberId", "token", "ttl"})
@EqualsAndHashCode(of = {"memberId", "token"})
public class RefreshTokenEntity {

	@Id
	private Long memberId;
	private String token;

	private long ttl;
}
