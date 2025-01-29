package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.domain.model.common.BaseTime;
import jakarta.persistence.Column;
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
    private Boolean kakaoWorkNotificationEnabled;
    private Boolean agitNotificationEnabled;
    private Boolean emailNotificationEnabled;
    private String imageUrl;
    private MemberStatus status;
    private String password;

    @Builder
    public Member(MemberInfo memberInfo, Boolean agitNotificationEnabled, Boolean emailNotificationEnabled, Boolean kakaoWorkNotificationEnabled,
                  Member admin, String imageUrl, MemberStatus status, String password) {
        this.memberInfo = memberInfo;
        this.agitNotificationEnabled = agitNotificationEnabled;
        this.emailNotificationEnabled = emailNotificationEnabled;
        this.kakaoWorkNotificationEnabled = kakaoWorkNotificationEnabled;
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
               .kakaoWorkNotificationEnabled(null)
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

    public void setStatusDeleted() {
        this.status = MemberStatus.DELETED;
    }
}
