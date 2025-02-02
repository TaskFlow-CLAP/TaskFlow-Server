package clap.server.application.service.member;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.VerifyPasswordUseCase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
class VerifyPasswordService implements VerifyPasswordUseCase {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void verifyPassword(Long memberId, String password) {
        Member member = memberService.findActiveMember(memberId);
        String encodedPassword = passwordEncoder.encode(password);
        member.verifyPassword(encodedPassword);
    }
}
