package clap.server.domain.model.member;

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
    private Member admin;
    private Boolean notificationEnabled;
    private String imageUrl;
    private MemberStatus status;
    private String password;

    @Builder
    public Member(MemberInfo memberInfo, Boolean notificationEnabled, Member admin, String imageUrl,
                  MemberStatus status, String password) {
        this.memberInfo = memberInfo;
        this.notificationEnabled = notificationEnabled;
        this.admin = admin;
        this.imageUrl = imageUrl;
        this.status = status;
        this.password = password;
    }

    public static Member createMember(Member admin, MemberInfo memberInfo) {
       return Member.builder()
               .memberInfo(memberInfo)
               .notificationEnabled(null)
               .admin(admin)
               .imageUrl(null)
               .status(MemberStatus.PENDING)
               .password(null)
               .build();
    }

    public void resetPassword(String newEncodedPassword) {
        this.password = newEncodedPassword;
    }

    public void resetPasswordAndActivateMember(String newEncodedPassword) {
        this.password = newEncodedPassword;
        this.status = MemberStatus.ACTIVE;
    }

    public String getNickname() {
        return memberInfo != null ? memberInfo.getNickname() : null;
    }

    public boolean isReviewer() {
        return this.memberInfo != null && this.memberInfo.isReviewer();
    }

    public void changeStatusToPending() {
        this.status = MemberStatus.PENDING;
    }
}
