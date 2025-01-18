package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.domain.model.common.BaseTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTime {
    private Long memberId;
    private MemberInfo memberInfo;
    private Boolean notificationEnabled;
    private String imageUrl;

    @Builder
    public Member(MemberInfo memberInfo, Boolean notificationEnabled, String imageUrl) {
        this.memberInfo = memberInfo;
        this.notificationEnabled = notificationEnabled;
        this.imageUrl = imageUrl;
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberInfo {
        private String name;
        private String email;
        private String nickname;
        private boolean isReviewer;
        private Department department;
        private MemberRole role;
        private String departmentRole;
        private Member admin;
        private MemberStatus memberStatus;
        private String password;

        @Builder
        public MemberInfo(String name, String email, String nickname, boolean isReviewer,
                        Department department, MemberRole role, String departmentRole,
                        Member admin, MemberStatus memberStatus, String password) {
            this.name = name;
            this.email = email;
            this.nickname = nickname;
            this.isReviewer = isReviewer;
            this.department = department;
            this.role = role;
            this.departmentRole = departmentRole;
            this.admin = admin;
            this.memberStatus = memberStatus;
            this.password = password;
        }
    }
}
