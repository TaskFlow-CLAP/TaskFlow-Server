package clap.server.application.service.notification;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.notification.EnableEmailUsecase;
import clap.server.application.port.inbound.notification.EnableKakaoUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class UpdateNotificationService implements EnableKakaoUsecase, EnableEmailUsecase {

    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;

    @Override
    @Deprecated
    public void enableEmail(Long memberId) {
        Member member = memberService.findActiveMember(memberId);
        member.updateEmailEnabled();
        commandMemberPort.save(member);
    }

    @Override
    @Deprecated
    public void enableKakao(Long memberId) {
        Member member = memberService.findActiveMember(memberId);
        member.updateKaKaoEnabled();
        commandMemberPort.save(member);
    }
}
