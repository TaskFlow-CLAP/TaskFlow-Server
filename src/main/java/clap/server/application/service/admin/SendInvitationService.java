    package clap.server.application.service.admin;

    import clap.server.adapter.inbound.web.dto.admin.SendInvitationRequest;
    import clap.server.adapter.outbound.api.dto.SendWebhookRequest;
    import clap.server.adapter.outbound.persistense.entity.notification.constant.NotificationType;
    import clap.server.application.port.inbound.admin.SendInvitationUsecase;
    import clap.server.application.port.outbound.member.LoadMemberPort;
    import clap.server.application.port.outbound.member.CommandMemberPort;
    import clap.server.application.port.outbound.webhook.SendEmailPort;
    import clap.server.domain.model.member.Member;
    import clap.server.exception.ApplicationException;
    import clap.server.exception.code.MemberErrorCode;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Service
    @RequiredArgsConstructor
    public class SendInvitationService implements SendInvitationUsecase {
        private final LoadMemberPort loadMemberPort;
        private final CommandMemberPort commandMemberPort;
        private final SendEmailPort sendEmailPort;

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

            // 이메일 전송
            sendEmailPort.sendEmail(
                    new SendWebhookRequest(
                            member.getMemberInfo().getEmail(),
                            NotificationType.INVITATION,      // 알림 유형
                            "회원가입 초대",                     // 작업 이름
                            member.getMemberInfo().getName(), // 회원 이름
                            member.getPassword(),             // 초기 비밀번호
                            null
                    )
            );
        }
    }
