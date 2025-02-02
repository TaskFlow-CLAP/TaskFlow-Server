package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.member.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;

public class MemberMapper {
    private MemberMapper() {
        throw new IllegalArgumentException();
    }

    public static MemberProfileResponse toMemberProfileResponse(Member member) {
        return new MemberProfileResponse(
                member.getMemberId(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getNickname(),
                member.getImageUrl(),
                member.getMemberInfo().getRole()
        );
    }

    public static MemberDetailInfoResponse toMemberDetailInfoResponse(Member member) {
        return new MemberDetailInfoResponse(
                member.getImageUrl(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getNickname(),
                member.getImageUrl(),
                member.isReviewer(),
                member.getMemberInfo().getRole(),
                member.getMemberInfo().getDepartment().getName(),
                member.getMemberInfo().getDepartmentRole(),
                toNotificationSettingInfoResponse(member)
        );
    }

    public static MemberDetailInfoResponse.NotificationSettingInfoResponse toNotificationSettingInfoResponse(Member member) {
        return new MemberDetailInfoResponse.NotificationSettingInfoResponse(
                member.getAgitNotificationEnabled(),
                member.getEmailNotificationEnabled(),
                member.getKakaoworkNotificationEnabled()
        );
    }
}