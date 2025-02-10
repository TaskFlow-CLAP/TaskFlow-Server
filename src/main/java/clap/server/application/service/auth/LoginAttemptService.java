package clap.server.application.service.auth;

import clap.server.application.port.inbound.auth.CheckAccountLockStatusUseCase;
import clap.server.application.port.outbound.auth.loginLog.CommandLoginLogPort;
import clap.server.application.port.outbound.auth.loginLog.LoadLoginLogPort;
import clap.server.domain.model.auth.LoginLog;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Service
@Transactional
public class LoginAttemptService implements CheckAccountLockStatusUseCase {
    private final LoadLoginLogPort loadLoginLogPort;
    private final CommandLoginLogPort commandLoginLogPort;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 30 * 60 * 1000; // 30분 (밀리초)

    public void recordFailedAttempt(String nickname, String clientIp) {
        LoginLog loginLog = loadLoginLogPort.findByNickname(clientIp).orElse(null);
        if (loginLog == null) {
            loginLog = LoginLog.createLoginLog(nickname, clientIp);
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

    @Override
    public void checkAccountIsLocked(String nickname) {
        LoginLog loginLog = loadLoginLogPort.findByNickname(nickname).orElse(null);
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
            else commandLoginLogPort.deleteById(nickname);
        }
    }


    public void resetFailedAttempts(String nickname) {
        commandLoginLogPort.deleteById(nickname);
    }
}
