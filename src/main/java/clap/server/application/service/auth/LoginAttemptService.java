package clap.server.application.service.auth;

import clap.server.application.port.outbound.auth.CommandLoginLogPort;
import clap.server.application.port.outbound.auth.LoadLoginLogPort;
import clap.server.domain.model.auth.LoginLog;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoginAttemptService {
    private final LoadLoginLogPort loadLoginLogPort;
    private final CommandLoginLogPort commandLoginLogPort;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 30 * 60 * 1000; // 30분 (밀리초)

    public void recordFailedAttempt(String sessionId, String clientIp, String attemptNickname) {
        LoginLog loginLog = loadLoginLogPort.findBySessionId(sessionId).orElse(null);
        if (loginLog == null) {
            loginLog = LoginLog.createLoginLog(sessionId, clientIp, attemptNickname);
        } else {
            int attemptCount = loginLog.recordFailedAttempt();
            if (attemptCount >= MAX_FAILED_ATTEMPTS) {
                loginLog.setLocked(true);
                commandLoginLogPort.save(loginLog);
                throw new AuthException(AuthErrorCode.ACCOUNT_IS_LOCKED);
            }
        }
        commandLoginLogPort.save(loginLog);
    }

    public void checkAccountIsLocked(String sessionId) {
        LoginLog loginLog = loadLoginLogPort.findBySessionId(sessionId).orElse(null);
        if (loginLog == null) {
            return;
        }

        if (loginLog.isLocked()) {
            LocalDateTime lastAttemptAt = loginLog.getLastAttemptAt();
            LocalDateTime now = LocalDateTime.now();

            long minutesSinceLastAttempt = ChronoUnit.MINUTES.between(lastAttemptAt, now);
            long minutesSinceLastAttemptInMillis = minutesSinceLastAttempt * 60 * 1000;
            if (minutesSinceLastAttemptInMillis <= LOCK_TIME_DURATION) {
                throw new AuthException(AuthErrorCode.ACCOUNT_IS_LOCKED);
            }
        }
    }


    public void resetFailedAttempts(String sessionId) {
        commandLoginLogPort.deleteById(sessionId);
    }
}
