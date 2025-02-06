package clap.server.domain.model.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.domain.model.common.BaseTime;
import clap.server.domain.model.task.Task;
import clap.server.exception.DomainException;
import clap.server.exception.code.MemberErrorCode;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;


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
    private Department department;



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
                .agitNotificationEnabled(false)
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

    public void changeToApproveRequested() {
        this.status = MemberStatus.APPROVAL_REQUEST;
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

    public void updateKaKaoEnabled() {
        if (!this.kakaoworkNotificationEnabled) {
            this.kakaoworkNotificationEnabled = true;
        }
        else {
            this.kakaoworkNotificationEnabled = false;
        }
    }

    public void updateAgitEnabled() {
        if (!this.agitNotificationEnabled) {
            this.agitNotificationEnabled = true;
        }
        else {
            this.agitNotificationEnabled = false;
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

    public static Boolean checkCommenter(Task task, Member member) {
        // 일반 회원일 경우 => 요청자인지 확인
        if ((member.getMemberInfo().getRole() == MemberRole.ROLE_USER)
                && !(Objects.equals(member.getMemberId(), task.getRequester().getMemberId()))) {
            throw new DomainException(MemberErrorCode.NOT_A_COMMENTER);
        }
        else {
            return true;
        }

    }

    public void verifyPassword(String encodedPassword) {
        if(!encodedPassword.equals(this.password)) {
            throw new DomainException(MemberErrorCode.PASSWORD_VERIFY_FAILED);
        }
    }

    public void register(Member admin) {
        this.admin = admin; // 관리자 설정
    }
}
