package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.UpdateMemberInfoRequest;
import clap.server.application.port.inbound.admin.ManageMemberUsecase;
import clap.server.application.port.inbound.domain.MemberService;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional
class ManageMemberService implements ManageMemberUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;

    @Override
    public void updateMemberInfo(Long memberId, UpdateMemberInfoRequest request) {
        Member member = memberService.findActiveMember(memberId);
        Department department = loadDepartmentPort.findById(request.departmentId()).orElseThrow(() -> new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        //TODO: 인프라팀만 담당자가 될 수 있도록 수정해야함
        member.getMemberInfo().updateMemberInfo(
                request.name(), request.email(), request.isReviewer(),
                department, request.role(), request.departmentRole());
        commandMemberPort.save(member);
    }
}
