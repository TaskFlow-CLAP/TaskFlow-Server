package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.exception.DomainException;
import clap.server.exception.code.MemberErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo {
    private String name;
    private String email;
    private String nickname;
    private boolean isReviewer;
    private Department department;
    private MemberRole role;
    private String departmentRole;

    @Builder
    public MemberInfo(String name, String email, String nickname, boolean isReviewer,
                      Department department, MemberRole role, String departmentRole) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.isReviewer = isReviewer;
        this.department = department;
        this.role = role;
        this.departmentRole = departmentRole;
    }

    public static MemberInfo toMemberInfo(String name, String email, String nickname, boolean isReviewer,
                                          Department department, MemberRole role, String departmentRole) {
        assertReviewerIsManager(isReviewer, role);
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

    public void updateMemberInfoByAdmin(String name, String email, boolean isReviewer,
                                        Department department, MemberRole role, String departmentRole) {
        assertReviewerIsManager(isReviewer, role);
        this.name = name;
        this.email = email;
        this.isReviewer = isReviewer;
        this.department = department;
        this.role = role;
        this.departmentRole = departmentRole;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public static void assertReviewerIsManager(boolean isReviewer, MemberRole role) {
        if (isReviewer) {
            if (role != MemberRole.ROLE_MANAGER) {
                throw new DomainException(MemberErrorCode.MANAGER_ONLY_REVIEW);
            }
        }
    }

}