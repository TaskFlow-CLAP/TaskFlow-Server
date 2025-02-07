package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.request.UpdateMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.MemberDetailsResponse;
import clap.server.application.mapper.response.MemberResponseMapper;
import clap.server.application.port.inbound.admin.MemberDetailUsecase;
import clap.server.application.port.inbound.admin.UpdateMemberUsecase;
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
class ManageMemberService implements UpdateMemberUsecase, MemberDetailUsecase {
    private final MemberService memberService;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;

    @Override
    @Transactional
    public void updateMemberInfo(Long adminId, Long memberId, UpdateMemberRequest request) {
        Member member = memberService.findById(memberId);
        Department department = loadDepartmentPort.findById(request.departmentId()).orElseThrow(() ->
                new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));

        //TODO: 인프라팀만 담당자가 될 수 있도록 수정해야함
        member.getMemberInfo().updateMemberInfoByAdmin(
                request.name(), request.isReviewer(),
                department, request.role(), request.departmentRole());
        commandMemberPort.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailsResponse getMemberDetail(Long memberId) {
        Member member = memberService.findById(memberId);
        return MemberResponseMapper.toMemberDetailsResponse(member);
    }
}
