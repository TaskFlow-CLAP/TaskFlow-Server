package clap.server.domain.policy.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.member.Department;
import clap.server.exception.DomainException;
import clap.server.exception.code.MemberErrorCode;

@Policy
public class ManagerDepartmentPolicy {
    public void validateDepartment(final Department department, final MemberRole memberRole) {
        if (!department.isManager() ){
            if(memberRole == MemberRole.ROLE_MANAGER){
                throw new DomainException(MemberErrorCode.MANAGER_PERMISSION_DENIED);
            }
        }
    }
}
