package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.admin.response.MemberDetailsResponse;
import clap.server.adapter.inbound.web.dto.member.response.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.response.MemberProfileResponse;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.member.MemberInfo;

public class MemberResponseMapper {
    private MemberResponseMapper() {
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
    public static Member toMember(MemberInfo memberInfo) {
        return Member.builder()
                .memberInfo(memberInfo)
                .agitNotificationEnabled(null)
                .emailNotificationEnabled(null)
                .kakaoworkNotificationEnabled(null)
                .admin(null)
                .imageUrl(null)
                .status(null)
                .password(null)
                .build();
    }

    public static MemberDetailsResponse toMemberDetailsResponse(Member member) {
        return new MemberDetailsResponse(
                member.getImageUrl(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getNickname(),
                member.getImageUrl(),
                member.isReviewer(),
                member.getMemberInfo().getRole(),
                member.getMemberInfo().getDepartment().getDepartmentId(),
                member.getMemberInfo().getDepartment().getName(),
                member.getMemberInfo().getDepartmentRole()
        );
    }

}