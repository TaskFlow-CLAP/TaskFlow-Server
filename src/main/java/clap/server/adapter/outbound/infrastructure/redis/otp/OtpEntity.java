package clap.server.adapter.outbound.infrastructure.redis.otp;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "OTP", timeToLive = 300) // 300초(5분) 후 자동 삭제
@Getter
@Builder
@ToString(of = {"email", "code"})
public class OtpEntity {
    @Id
    private String email;
    private String code;
}
