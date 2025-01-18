package clap.server.application;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.port.inbound.management.RegisterMemberUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static clap.server.application.mapper.MemberInfoMapper.toMemberInfo;
import static clap.server.application.mapper.MemberMapper.toMember;

@ApplicationService
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
        Member admin = loadMemberPort.findById(adminId).orElse(null);
        Department department = loadDepartmentPort.findById(1l).orElse(null);
        MemberInfo memberInfo = toMemberInfo(request.name(), request.email(), request.nickname(), request.isReviewer(),
                department, MemberRole.ROLE_USER, request.departmentRole());
        Member member = toMember(memberInfo);
        member.register(admin);
        commandMemberPort.save(member);
    }
}
