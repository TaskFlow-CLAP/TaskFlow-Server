package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.request.SendInvitationRequest;
import clap.server.application.port.inbound.admin.SendInvitationUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.utils.InitialPasswordGenerator;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class SendInvitationService implements SendInvitationUsecase {
    private final LoadMemberPort loadMemberPort;
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final SendInvitationEmailService sendInvitationEmailService;
    private final InitialPasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendInvitation(SendInvitationRequest request) {
        Member member = memberService.findMemberWithDepartment(request.memberId());

        String initialPassword = passwordGenerator.generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(initialPassword);

        member.resetPassword(encodedPassword);

        member.changeToApproveRequested();

        commandMemberPort.save(member);

        sendInvitationEmailService.sendInvitationEmail(
                member.getMemberInfo().getEmail(),
                member.getMemberInfo().getName(),
                initialPassword,
                member.getNickname()
        );
    }
}