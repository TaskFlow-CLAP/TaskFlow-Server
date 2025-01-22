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

    public void register(Member admin) {
        this.admin = admin;
        this.notificationEnabled = null;
        this.imageUrl = null;
        this.status = MemberStatus.PENDING;
        this.password = null;
    }
}
