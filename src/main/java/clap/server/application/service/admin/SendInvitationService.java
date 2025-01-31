package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.SendInvitationRequest;
import clap.server.application.port.inbound.admin.SendInvitationUsecase;
import clap.server.application.port.outbound.email.SendInvitationEmailPort;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class SendInvitationService implements SendInvitationUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;
    private final SendInvitationEmailPort sendInvitationEmailPort;

    @Override
    @Transactional
    public void sendInvitation(SendInvitationRequest request) {
        // 회원 조회
        Member member = loadMemberPort.findById(request.memberId())
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 회원 상태를 PENDING으로 변경
        member.changeStatusToPending();

        // 변경된 회원 저장
        commandMemberPort.save(member);

        // 초대 이메일 전송
        sendInvitationEmailPort.sendInvitationEmail(
                request,
                member.getMemberInfo().getEmail(),
                member.getPassword()           //TODO: 초기 비밀번호 생성 로직 추가
        );
    }
}
