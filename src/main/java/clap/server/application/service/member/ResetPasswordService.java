package clap.server.application.service.member;

import clap.server.application.port.inbound.auth.ResetPasswordUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional
@Slf4j
class ResetPasswordService implements ResetPasswordUsecase {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final CommandMemberPort commandMemberPort;

    @Override
    public void resetPassword(Long memberId, String inputPassword) {
        Member member = memberService.findActiveMember(memberId);
        String encodedPassword = passwordEncoder.encode(inputPassword);
        member.resetPassword(encodedPassword);
        commandMemberPort.save(member);
    }

    @Override
    public void resetPasswordAndActivateMember(Long memberId, String password) {
        Member member = memberService.findById(memberId);
        member.resetPasswordAndActivateMember(passwordEncoder.encode(password));
        commandMemberPort.save(member);
    }
}
