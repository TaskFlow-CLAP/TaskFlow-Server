package clap.server.application.service.auth;

import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.application.mapper.response.AuthResponseMapper;
import clap.server.application.port.inbound.auth.AuthUsecase;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
class AuthService implements AuthUsecase {
    private final LoadMemberPort loadMemberPort;
    private final IssueTokenService issueTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(String nickname, String password) {
        Member member = loadMemberPort.findByNickname(nickname).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (passwordEncoder.matches(password, member.getPassword())) {
            CustomJwts jwts = issueTokenService.createToken(member);
            return AuthResponseMapper.toLoginResponse(
                    jwts.accessToken(),
                    jwts.refreshToken(),
                    member
            );
        } else {
            throw new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }
}
