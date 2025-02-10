package clap.server.application.service.auth;

import clap.server.application.port.outbound.auth.loginLog.CommandLoginLogPort;
import clap.server.application.port.outbound.auth.loginLog.LoadLoginLogPort;
import clap.server.domain.model.auth.LoginLog;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Mock
    private LoadLoginLogPort loadLoginLogPort;

    @Mock
    private CommandLoginLogPort commandLoginLogPort;

    private static final String nickname = "testUser";
    private static final String clientIp = "192.168.1.1";
    private LoginLog existingLoginLog;
    private LoginLog lockedAccountLoginLog;
    private LoginLog lockTimeExpiredLoginLog;

    public static LoginLog createLoginLog(int count, boolean isLocked, LocalDateTime lastAttemptAt){
        return LoginLog.builder()
                .nickname(nickname)
                .lastAttemptAt(lastAttemptAt)
                .failedCount(count)
                .isLocked(isLocked)
                .clientIp(clientIp)
                .build();
    }

    @BeforeEach
    void setUp() {
        existingLoginLog = createLoginLog(3, false, LocalDateTime.now());
        lockedAccountLoginLog = createLoginLog(5, true, LocalDateTime.now());
        lockTimeExpiredLoginLog = createLoginLog(5, true, LocalDateTime.now().minusMinutes(31));
    }

    @Test
    @DisplayName("로그인에 실패하면 nickname을 통해 로그인 실패 기록이 저장된다.")
    void recordFailedAttempt_NewIP() {
        String nickname = "testUser";
        when(loadLoginLogPort.findByNickname(nickname)).thenReturn(Optional.empty());

        loginAttemptService.recordFailedAttempt(nickname, nickname);

        verify(commandLoginLogPort).save(any(LoginLog.class));
    }

    @Test
    @DisplayName("기존 IP로 로그인에 실패하면 로그인 실패 기록이 갱신된다.")
    void recordFailedAttempt_ExistingIP_BeforeLock() {
        LoginLog existingLog = existingLoginLog;;

        when(loadLoginLogPort.findByNickname(nickname)).thenReturn(Optional.of(existingLog));

        loginAttemptService.recordFailedAttempt(nickname, clientIp);

        verify(commandLoginLogPort).save(existingLog);
        assertEquals(4, existingLog.getFailedCount());
        assertFalse(existingLog.isLocked());
    }

    @Test
    @DisplayName("로그인에 5회 실패하면 계정이 잠긴다.")
    void recordFailedAttempt_AccountLock() {
        LoginLog existingLog = existingLoginLog;
        existingLog.recordFailedAttempt();

        when(loadLoginLogPort.findByNickname(nickname)).thenReturn(Optional.of(existingLog));

        assertThrows(AuthException.class, () -> loginAttemptService.recordFailedAttempt(nickname,clientIp ));

        verify(commandLoginLogPort).save(existingLog);
        assertTrue(existingLog.isLocked());
    }

    @Test
    @DisplayName("로그인 5회 실패가 아닐시에는 계정 잠금처리가 되지 않는다.")
    void checkAccountIsLocked_NotLocked() {
        LoginLog loginLog = existingLoginLog;
        loginLog.setLocked(false);

        when(loadLoginLogPort.findByNickname(loginLog.getNickname())).thenReturn(Optional.of(loginLog));

        assertDoesNotThrow(() -> loginAttemptService.checkAccountIsLocked(loginLog.getNickname()));
    }

    @Test
    @DisplayName("잠긴 계정에 대해 로그인 시도시에 AUTH_012 예외가 발생한다.")
    void checkAccountIsLocked_Locked() {
        LoginLog loginLog = lockedAccountLoginLog;

        when(loadLoginLogPort.findByNickname(loginLog.getNickname())).thenReturn(Optional.of(loginLog));

        AuthException exception = assertThrows(AuthException.class, () ->
                loginAttemptService.checkAccountIsLocked(loginLog.getNickname())
        );

        assertEquals(AuthErrorCode.ACCOUNT_IS_LOCKED.getMessage(), exception.getMessage(),
                "잠긴 계정에 대해 로그인 시도시에 AUTH_012 예외가 발생해야 합니다.");
    }

    @Test
    @DisplayName("30분이 지난 후 잠김 여부를 확인하면 AUTH_012 예외를 반환하지 않는다.")
    void checkAccountIsLocked_LockTimeExpired() {
        LoginLog loginLog = lockTimeExpiredLoginLog;

        when(loadLoginLogPort.findByNickname(loginLog.getNickname())).thenReturn(Optional.of(loginLog));

        loginAttemptService.checkAccountIsLocked(loginLog.getNickname());

        verify(commandLoginLogPort).deleteById(loginLog.getNickname());
    }

}
