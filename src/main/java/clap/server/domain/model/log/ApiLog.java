package clap.server.domain.model.log;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiLog {
    private Long logId;
    private Integer statusCode;
    private String memberId;
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
    private String logType;

    // '로그인 시도'인지 확인
    public boolean isLoginAttempt() {
        return "로그인 시도".equals(this.logType);
    }

    // 익명 사용자 여부 확인
    public boolean isAnonymousUser() {
        return this.memberId == null || this.memberId.isBlank();
    }

    // 비즈니스 로직: 회원 로그 여부 확인
    public boolean isMemberLog() {
        return !isAnonymousUser() && !"로그인 시도".equals(this.logType);
    }
}
