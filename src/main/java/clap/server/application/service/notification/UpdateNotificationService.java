package clap.server.application.service.notification;

import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.inbound.notification.EnableAgitUsecase;
import clap.server.application.port.inbound.notification.EnableEmailUsecase;
import clap.server.application.port.inbound.notification.EnableKakaoUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class UpdateNotificationService implements EnableKakaoUsecase, EnableAgitUsecase, EnableEmailUsecase {

    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;

    @Override
    public void enableAgit(Long userId) {
        Member member = memberService.findActiveMember(userId);
        member.updateAgitEnabled();
        commandMemberPort.save(member);
    }

    @Override
    public void enableEmail(Long userId) {
        Member member = memberService.findActiveMember(userId);
        member.updateEmailEnabled();
        commandMemberPort.save(member);
    }

    @Override
    public void enableKakao(Long userId) {
        Member member = memberService.findActiveMember(userId);
        member.updateKaKaoEnabled();
        commandMemberPort.save(member);
    }
}
