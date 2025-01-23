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
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
class AuthService implements AuthUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandRefreshTokenPort commandRefreshTokenPort;
    private final IssueTokenService issueTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(String nickname, String password) {
        Member member = loadMemberPort.findByNickname(nickname).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
        validatePassword(password, member.getPassword());

        if (member.getStatus().equals(MemberStatus.APPROVAL_REQUEST)) {
            String temporaryToken = issueTokenService.issueTemporaryToken(member.getMemberId());
            return AuthResponseMapper.toLoginResponse(
                    temporaryToken, null, member
            );
        } else {
            CustomJwts jwtTokens = issueTokenService.issueTokens(member);
            commandRefreshTokenPort.save(
                issueTokenService.issueRefreshToken(member.getMemberId())
            );
            return AuthResponseMapper.toLoginResponse(
                    jwtTokens.accessToken(), jwtTokens.refreshToken(), member
            );
        }
    }

    private void validatePassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }

}
