package clap.server.application.service.admin;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.port.inbound.admin.DeleteMemberUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Member;
import clap.server.domain.policy.member.ManagerInfoUpdatePolicy;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;
    private final ManagerInfoUpdatePolicy managerInfoUpdatePolicy;

    @Transactional
    @Override
    public void deleteMember(Long memberId) {
        Member member = loadMemberPort.findById(memberId)
                .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));

        if(member.getMemberInfo().getRole().equals(MemberRole.ROLE_MANAGER)){
            managerInfoUpdatePolicy.validateNoRemainingTasks(member);
        }
        Hibernate.initialize(member.getDepartment());
        member.softDelete();
        commandMemberPort.save(member);
    }
}
