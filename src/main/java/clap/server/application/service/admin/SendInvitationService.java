package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.SendInvitationRequest;
import clap.server.application.port.inbound.admin.SendInvitationUsecase;
import clap.server.application.port.outbound.email.SendInvitationEmailPort;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.common.utils.InitialPasswordGenerator;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class SendInvitationService implements SendInvitationUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;
    private final SendInvitationEmailPort sendInvitationEmailPort;
    private final InitialPasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void sendInvitation(SendInvitationRequest request) {
        // 회원 조회
        Member member = loadMemberPort.findById(request.memberId())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 초기 비밀번호 생성
        String initialPassword = passwordGenerator.generateRandomPassword(8);
        String encodedPassword = passwordEncoder.encode(initialPassword);

        // 회원 비밀번호 업데이트
        member.resetPassword(encodedPassword);
        commandMemberPort.save(member);

        // 회원 상태를 APPROVAL_REQUEST으로 변경
        member.changeStatusToAPPROVAL_REQUEST();

        sendInvitationEmailPort.sendInvitationEmail(
                member.getMemberInfo().getEmail(),
                member.getMemberInfo().getName(),
                initialPassword
        );
    }
}
