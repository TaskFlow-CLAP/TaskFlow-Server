package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;
import clap.server.application.port.inbound.admin.RegisterMemberUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
class RegisterMemberService implements RegisterMemberUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerMember(Long adminId, RegisterMemberRequest request) {
        Member admin = memberService.findActiveMember(adminId);
        Department department = loadDepartmentPort.findById(request.departmentId()).orElseThrow(()->
                new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        MemberInfo memberInfo = MemberInfo.toMemberInfo(request.name(), request.email(), request.nickname(), request.isReviewer(),
                department, request.role(), request.departmentRole());
        Member member = Member.createMember(admin, memberInfo);
        commandMemberPort.save(member);
    }
}
