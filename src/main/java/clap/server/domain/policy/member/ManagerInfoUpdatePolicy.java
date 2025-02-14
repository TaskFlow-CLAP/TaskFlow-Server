package clap.server.domain.policy.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.Member;
import clap.server.exception.DomainException;
import clap.server.exception.code.MemberErrorCode;

@Policy
public class ManagerInfoUpdatePolicy {

    // 담당자 권한이 있는 부서의 인원만 담당자의 역할이 허용됨
    public void validateDepartment(final Department department, final MemberRole memberRole) {
        if (!department.isManager() ){
            if(memberRole == MemberRole.ROLE_MANAGER){
                throw new DomainException(MemberErrorCode.MANAGER_PERMISSION_DENIED);
            }
        }
    }

    // 담당자의 잔여 작업이 남아있는 경우 다른 역할로의 변경이 허용되지 않음
    public void validateNoRemainingTasks(final Member member){
        if(member.getInReviewingTaskCount()>0 || member.getInProgressTaskCount()> 0){
            throw new DomainException(MemberErrorCode.ROLE_CHANGE_NOT_ALLOWED_WITH_TASKS);
        }
    }
}
