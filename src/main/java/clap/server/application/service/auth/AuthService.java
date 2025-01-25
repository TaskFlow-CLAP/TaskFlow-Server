package clap.server.application.service.auth;

import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.application.mapper.response.AuthResponseMapper;
import clap.server.application.port.inbound.auth.AuthUsecase;
import clap.server.application.port.outbound.auth.CommandRefreshTokenPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.member.Member;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
class AuthService implements AuthUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandRefreshTokenPort commandRefreshTokenPort;
    private final IssueTokenService issueTokenService;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    @Override
    @Transactional
    public LoginResponse login(String nickname, String password, String sessionId, String clientIp) {
        Member member = getMember(nickname, sessionId, clientIp);

        validatePassword(password, member.getPassword(), sessionId, nickname, clientIp);

        if (member.getStatus().equals(MemberStatus.APPROVAL_REQUEST)) {
            String temporaryToken = issueTokenService.issueTemporaryToken(member.getMemberId());
            return AuthResponseMapper.toLoginResponse(temporaryToken, null, member);
        }

        CustomJwts jwtTokens = issueTokenService.issueTokens(member);
        commandRefreshTokenPort.save(issueTokenService.issueRefreshToken(member.getMemberId()));
        loginAttemptService.resetFailedAttempts(sessionId);
        return AuthResponseMapper.toLoginResponse(jwtTokens.accessToken(), jwtTokens.refreshToken(), member);
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

