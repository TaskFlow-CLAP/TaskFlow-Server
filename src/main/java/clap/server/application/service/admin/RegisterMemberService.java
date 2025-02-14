package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.request.RegisterMemberRequest;
import clap.server.application.port.inbound.admin.RegisterMemberUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.domain.policy.member.ManagerInfoUpdatePolicy;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@ApplicationService
@RequiredArgsConstructor
class RegisterMemberService implements RegisterMemberUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;
    private final LoadMemberPort loadMemberPort;
    private final ManagerInfoUpdatePolicy managerInfoUpdatePolicy;

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
        Member admin = memberService.findActiveMember(adminId);
        Department department = loadDepartmentPort.findById(request.departmentId())
                .orElseThrow(() -> new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        if (loadMemberPort.existsByNicknamesOrEmails(Set.of(request.nickname()), Set.of(request.email()))) {
            throw new ApplicationException(MemberErrorCode.DUPLICATE_NICKNAME_OR_EMAIL);
        }

        managerInfoUpdatePolicy.validateDepartment(department, request.role());
        MemberInfo memberInfo = MemberInfo.toMemberInfo(request.name(), request.email(), request.nickname(), request.isReviewer(),
                department, request.role(), request.departmentRole());
        Member member = Member.createMember(admin, memberInfo);
        commandMemberPort.save(member);
    }

}
