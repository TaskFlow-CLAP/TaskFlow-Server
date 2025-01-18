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
    private MemberStatus memberStatus;
    private String password;

    @Builder
    public Member(MemberInfo memberInfo, Boolean notificationEnabled, String imageUrl,
                  MemberStatus memberStatus, String password) {
        this.memberInfo = memberInfo;
        this.notificationEnabled = notificationEnabled;
        this.imageUrl = imageUrl;
        this.memberStatus = memberStatus;
        this.password = password;
    }

    public void register(Member Admin){
        this.notificationEnabled=null;
        this.imageUrl=null;
        this.memberStatus=MemberStatus.PENDING;
        this.password="";
    }
}
