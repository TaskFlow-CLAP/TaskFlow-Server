package clap.server.adapter.outbound.infrastructure.redis.log;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "loginLog", timeToLive = 3600)
@Builder
@ToString(of = {"nickname", "clientIp", "lastAttemptAt", "failedCount", "isLocked"})
@EqualsAndHashCode(of = {"nickname"})
public class LoginLogEntity {
	@Id
	private String nickname;

	private String clientIp;

	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Builder.Default
	private LocalDateTime lastAttemptAt = LocalDateTime.now();

	private int failedCount;

	private boolean isLocked;
}
