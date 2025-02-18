package clap.server.application.service.admin;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.policy.member.ManagerInfoUpdatePolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final ManagerInfoUpdatePolicy managerInfoUpdatePolicy;

    @Transactional
    @Override
    public void deleteMember(Long memberId) {
        Member member = memberService.findMemberWithDepartment(memberId);

        if (member.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER) {
            managerInfoUpdatePolicy.validateNoRemainingTasks(member);
        }
        member.softDelete();
        commandMemberPort.save(member);
    }
}
