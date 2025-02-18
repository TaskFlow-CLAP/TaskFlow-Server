package clap.server.application.service.admin;

import clap.server.adapter.inbound.web.dto.admin.request.UpdateMemberRequest;
import clap.server.adapter.inbound.web.dto.admin.response.MemberDetailsResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.application.mapper.response.MemberResponseMapper;
import clap.server.application.port.inbound.admin.MemberDetailUsecase;
import clap.server.application.port.inbound.admin.UpdateMemberUsecase;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadDepartmentPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.domain.policy.member.ManagerInfoUpdatePolicy;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.DepartmentErrorCode;
import clap.server.exception.code.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
class ManageMemberService implements UpdateMemberUsecase, MemberDetailUsecase {
    private final LoadMemberPort loadMemberPort;
    private final CommandMemberPort commandMemberPort;
    private final LoadDepartmentPort loadDepartmentPort;
    private final ManagerInfoUpdatePolicy managerInfoUpdatePolicy;

    @Override
    @Transactional
    public void updateMemberInfo(Long adminId, Long memberId, UpdateMemberRequest request) {
        Member member = loadMemberPort.findByIdWithFetchDepartment(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
        Department department = loadDepartmentPort.findById(request.departmentId()).orElseThrow(() ->
                new ApplicationException(DepartmentErrorCode.DEPARTMENT_NOT_FOUND));
        managerInfoUpdatePolicy.validateDepartment(department, request.role());
        if(member.getMemberInfo().getRole() == MemberRole.ROLE_MANAGER && !(request.role()==MemberRole.ROLE_MANAGER)){
            managerInfoUpdatePolicy.validateNoRemainingTasks(member);
        }

        member.getMemberInfo().updateMemberInfoByAdmin(
                request.name(), request.isReviewer(),
                department, request.role(), request.departmentRole());
        commandMemberPort.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDetailsResponse getMemberDetail(Long memberId) {
        Member member = loadMemberPort.findByIdWithFetchDepartment(memberId).orElseThrow(
                () -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));;
        return MemberResponseMapper.toMemberDetailsResponse(member);
    }
}
