package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogTypeEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiLog {
    private Long logId;
    private Integer statusCode;
    private LocalDateTime requestAt;
    private LocalDateTime responseAt;
    private String dtype;
    private String request;
    private String requestUrl;
    private String response;
    private String clientIp;
    private String customStatusCode;
    private String serverIp;
    private String requestMethod;
    private LogTypeEnum logType;
    private String userId;

    public boolean isLoginAttempt() {
        return "로그인 시도".equals(this.logType);
    }

    public boolean isAnonymousUser() {
        return this.userId == null || this.userId.isBlank();
    }

    public boolean isMemberLog() {
        return !isAnonymousUser() && !"로그인 시도".equals(this.logType);
    }
}
