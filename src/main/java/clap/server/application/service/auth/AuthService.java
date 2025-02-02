package clap.server.application.service.auth;

import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.application.mapper.response.AuthResponseMapper;
import clap.server.application.port.inbound.auth.LoginUsecase;
import clap.server.application.port.inbound.auth.LogoutUsecase;
import clap.server.application.port.outbound.auth.ForbiddenTokenPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.auth.ForbiddenToken;
import clap.server.domain.model.auth.RefreshToken;
import clap.server.domain.model.member.Member;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
@Transactional
class AuthService implements LoginUsecase, LogoutUsecase {
    private final LoadMemberPort loadMemberPort;
    private final ManageTokenService manageTokenService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final RefreshTokenService refreshTokenService;
    private final ForbiddenTokenPort forbiddenTokenPort;


    @Override
    public LoginResponse login(String nickname, String password, String sessionId, String clientIp) {
        Member member = getMember(nickname, sessionId, clientIp);

        validatePassword(password, member.getPassword(), sessionId, nickname, clientIp);

        if (member.getStatus().equals(MemberStatus.APPROVAL_REQUEST)) {
            String temporaryToken = manageTokenService.issueTemporaryToken(member.getMemberId());
            return AuthResponseMapper.toLoginResponse(temporaryToken, null, member);
        }

        CustomJwts jwtTokens = manageTokenService.issueTokens(member);
        refreshTokenService.saveRefreshToken(manageTokenService.issueRefreshToken(member.getMemberId()));
        loginAttemptService.resetFailedAttempts(sessionId);
        return AuthResponseMapper.toLoginResponse(jwtTokens.accessToken(), jwtTokens.refreshToken(), member);
    }

    @Override
    public void logout(Long memberId, String accessToken, String refreshToken) {
        RefreshToken refreshTokenFindByMember = refreshTokenService.getRefreshToken(memberId);
        refreshTokenService.validateToken(refreshToken, refreshTokenFindByMember);
        refreshTokenService.deleteRefreshToken(refreshTokenFindByMember);
        deleteAccessToken(memberId, accessToken);
    }

    private void deleteAccessToken(Long memberId, String accessToken) {
        LocalDateTime expiredDate = manageTokenService.getExpiredDate(accessToken);

        LocalDateTime now = LocalDateTime.now();
        long timeToLive = Duration.between(now, expiredDate).toSeconds();

        ForbiddenToken forbiddenToken = ForbiddenToken.of(accessToken, memberId, timeToLive);
        forbiddenTokenPort.save(forbiddenToken);
    }

    private Member getMember(String inputNickname, String sessionId, String clientIp) {
        return loadMemberPort.findByNickname(inputNickname).orElseThrow(() ->
        {
            loginAttemptService.recordFailedAttempt(sessionId, clientIp, inputNickname);
            return new AuthException(AuthErrorCode.LOGIN_REQUEST_FAILED);
        });
    }

    private void validatePassword(String inputPassword, String encodedPassword, String sessionId, String inputNickname, String clientIp) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            loginAttemptService.recordFailedAttempt(sessionId, clientIp, inputNickname);
            throw new AuthException(AuthErrorCode.LOGIN_REQUEST_FAILED);
        }
    }

}

