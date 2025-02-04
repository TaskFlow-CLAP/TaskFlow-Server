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
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@RedisHash("loginLog")
@Builder
@ToString(of = {"clientIp", "attemptNickname", "lastAttemptAt", "attemptCount", "isLocked"})
@EqualsAndHashCode(of = {"clientIp"})
public class LoginLogEntity {
	@Id
	private String clientIp;

	private String attemptNickname;

	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Builder.Default
	private LocalDateTime lastAttemptAt = LocalDateTime.now();

	private int attemptCount;

	private boolean isLocked;
}
