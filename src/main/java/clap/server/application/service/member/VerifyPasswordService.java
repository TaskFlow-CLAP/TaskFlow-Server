package clap.server.application.service.member;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.VerifyPasswordUseCase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.AuthException;
import clap.server.exception.code.AuthErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
class VerifyPasswordService implements VerifyPasswordUseCase {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void verifyPassword(Long memberId, String inputPassword) {
        Member member = memberService.findActiveMember(memberId);
        if (!passwordEncoder.matches(member.getPassword(), inputPassword)) {
            throw new ApplicationException(MemberErrorCode.PASSWORD_VERIFY_FAILED);
        }
    }
}
