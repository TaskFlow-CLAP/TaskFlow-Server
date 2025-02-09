package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Boolean emailNotificationEnabled;
    private String imageUrl;
    private MemberStatus status;
    private String password;
    private Department department;

    public Member(MemberInfo memberInfo, Boolean emailNotificationEnabled, Boolean kakaoworkNotificationEnabled,
                  Member admin, String imageUrl, MemberStatus status, String password) {
        this.memberInfo = memberInfo;
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
                .emailNotificationEnabled(false)
                .kakaoworkNotificationEnabled(false)
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
        this.emailNotificationEnabled = true;
        this.kakaoworkNotificationEnabled = true;
    }

    public String getNickname() {
        return memberInfo != null ? memberInfo.getNickname() : null;
    }

    public boolean isReviewer() {
        return this.memberInfo != null && this.memberInfo.isReviewer();
    }

    public void changeToApproveRequested() {
        this.status = MemberStatus.APPROVAL_REQUEST;
    }

    public void updateMemberInfo(String name, Boolean emailNotificationEnabled, Boolean kakaoWorkNotificationEnabled) {
        this.memberInfo.updateName(name);
        this.emailNotificationEnabled = emailNotificationEnabled;
        this.kakaoworkNotificationEnabled = kakaoWorkNotificationEnabled;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStatusDeleted() {
        this.status = MemberStatus.DELETED;
    }

    public void updateKaKaoEnabled() {
        if (!this.kakaoworkNotificationEnabled) {
            this.kakaoworkNotificationEnabled = true;
        }
        else {
            this.kakaoworkNotificationEnabled = false;
        }
    }

    public void updateEmailEnabled() {
        if (!this.emailNotificationEnabled) {
            this.emailNotificationEnabled = true;
        }
        else {
            this.emailNotificationEnabled = false;
        }
    }

    public void register(Member admin) {
        this.admin = admin;
    }
}
