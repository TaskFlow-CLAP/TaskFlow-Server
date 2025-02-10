package clap.server.application.service.auth;

import clap.server.TestDataFactory;
import clap.server.adapter.inbound.web.dto.auth.response.LoginResponse;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.member.Member;
import clap.server.exception.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private LoadMemberPort loadMemberPort;
    @Mock
    private ManageTokenService manageTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private RefreshTokenService refreshTokenService;

    private Member user;
    private Member notApprovedUser;

    @BeforeEach
    void setUp() {
        user = TestDataFactory.createUser();
        notApprovedUser = TestDataFactory.createNotApprovedUser();
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        // Given
        String nickname = "atom.user";
        String inputPassword = "Password000!";
        String clientIp = "127.0.0.1";
        Member member = user;
        CustomJwts jwtTokens = new CustomJwts("accessToken", "refreshToken");

        when(loadMemberPort.findByNickname(nickname)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(inputPassword, member.getPassword())).thenReturn(true);
        when(manageTokenService.issueTokens(member)).thenReturn(jwtTokens);

        // When
        LoginResponse response = authService.login(nickname, inputPassword, clientIp);

        // Then
        assertNotNull(response);
        assertEquals(jwtTokens.accessToken(), response.accessToken());
        assertEquals(jwtTokens.refreshToken(), response.refreshToken());
        verify(loginAttemptService).resetFailedAttempts(clientIp);
        verify(refreshTokenService).saveRefreshToken(any());
    }

    @Test
    @DisplayName("잘못된 비밀번호를 입력하면 로그인 실패한다.")
    void loginFailureWrongPassword() {
        // Given
        String nickname = "atom.user";
        String inputPassword = "wrongPassword000!";
        String clientIp = "127.0.0.1";
        Member member = user;

        when(loadMemberPort.findByNickname(nickname)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(inputPassword, member.getPassword())).thenReturn(false);

        // When & Then
        assertThrows(AuthException.class, () -> authService.login(nickname, inputPassword, clientIp));
        verify(loginAttemptService).recordFailedAttempt(clientIp, nickname);
    }


    @Test
    @DisplayName("사용자가 초기 로그인 시 임시 토큰이 발급된다.")
    void loginWithApprovalRequestStatus() {
        // Given
        String nickname = "atom.user";
        String inputPassword = "Password000!";
        String clientIp = "127.0.0.1";

        Member member = notApprovedUser;
        String temporaryToken = "temporaryToken";

        when(loadMemberPort.findByNickname(nickname)).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(inputPassword, member.getPassword())).thenReturn(true);
        when(manageTokenService.issueTemporaryToken(notApprovedUser.getMemberId())).thenReturn(temporaryToken);

        // When
        LoginResponse response = authService.login(nickname, inputPassword, clientIp);

        // Then
        assertNotNull(response);
        assertEquals(temporaryToken, response.accessToken());
        assertNull(response.refreshToken());
        verify(manageTokenService).issueTemporaryToken(notApprovedUser.getMemberId());
        verify(manageTokenService, never()).issueTokens(any());
        verify(refreshTokenService, never()).saveRefreshToken(any());
        verify(loginAttemptService, never()).resetFailedAttempts(any());
    }

}
