package clap.server.application.mapper;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.domain.model.member.Department;
import clap.server.domain.model.member.MemberInfo;

public class MemberInfoMapper {
    private MemberInfoMapper() {
        throw new IllegalArgumentException();
    }

    public static MemberInfo toMemberInfo(String name, String email, String nickname, boolean isReviewer,
                                          Department department, MemberRole role, String departmentRole) {
        return MemberInfo.builder()
                .name(name)
                .email(email)
                .nickname(nickname)
                .isReviewer(isReviewer)
                .department(department)
                .role(role)
                .departmentRole(departmentRole)
                .build();
    }
}
