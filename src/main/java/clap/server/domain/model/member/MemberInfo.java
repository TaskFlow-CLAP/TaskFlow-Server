package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import lombok.*;
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

}