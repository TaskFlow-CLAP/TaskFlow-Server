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
    private Boolean kakaoworkNotificationEnabled;
    private Boolean agitNotificationEnabled;
    private Boolean emailNotificationEnabled;
    private String imageUrl;
    private MemberStatus status;
    private String password;

    @Builder
    public Member(MemberInfo memberInfo, Boolean agitNotificationEnabled, Boolean emailNotificationEnabled, Boolean kakaoworkNotificationEnabled,
                  Member admin, String imageUrl, MemberStatus status, String password) {
        this.memberInfo = memberInfo;
        this.agitNotificationEnabled = agitNotificationEnabled;
        this.emailNotificationEnabled = emailNotificationEnabled;
        this.kakaoworkNotificationEnabled = kakaoworkNotificationEnabled;
        this.admin = admin;
        this.imageUrl = imageUrl;
        this.status = status;
        this.password = password;
    }

    public static Member createMember(Member admin, MemberInfo memberInfo) {
        return Member.builder()
                .memberInfo(memberInfo)
                .agitNotificationEnabled(null)
                .emailNotificationEnabled(null)
                .kakaoworkNotificationEnabled(null)
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
        this.agitNotificationEnabled = true;
        this.emailNotificationEnabled = true;
        this.kakaoworkNotificationEnabled = true;
    }

    public String getNickname() {
        return memberInfo != null ? memberInfo.getNickname() : null;
    }

    public boolean isReviewer() {
        return this.memberInfo != null && this.memberInfo.isReviewer();
    }

    public void updateMemberInfo(String name, Boolean agitNotificationEnabled, Boolean emailNotificationEnabled, Boolean kakaoWorkNotificationEnabled, String imageUrl) {
        this.memberInfo.updateName(name);
        this.agitNotificationEnabled = agitNotificationEnabled;
        this.emailNotificationEnabled = emailNotificationEnabled;
        this.kakaoworkNotificationEnabled = kakaoWorkNotificationEnabled;
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public void setStatusDeleted() {
        this.status = MemberStatus.DELETED;
    }
}
