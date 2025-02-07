package clap.server.application.service.member;

import clap.server.adapter.inbound.web.dto.member.request.SendInitialPasswordRequest;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.member.ResetInitialPasswordUsecase;
import clap.server.application.port.inbound.member.ResetPasswordUsecase;
import clap.server.application.port.inbound.member.SendNewPasswordUsecase;
import clap.server.application.port.outbound.email.SendEmailPort;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.utils.InitialPasswordGenerator;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional
@Slf4j
class ResetPasswordService implements ResetPasswordUsecase, ResetInitialPasswordUsecase, SendNewPasswordUsecase {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final CommandMemberPort commandMemberPort;
    private final LoadMemberPort loadMemberPort;
    private final InitialPasswordGenerator initialPasswordGenerator;
    private final SendEmailPort sendEmailPort;

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

    @Override
    public void sendInitialPassword(SendInitialPasswordRequest request) {
        Member member = loadMemberPort.findByNameAndEmail(request.name(), request.email())
                .orElseThrow(
                        () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        String newPassword = initialPasswordGenerator.generateRandomPassword();
        sendEmailPort.sendNewPasswordEmail(request.email(), request.name(), newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.resetPassword(encodedPassword);
        commandMemberPort.save(member);
    }
}
