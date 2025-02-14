package clap.server.application.mapper.response;


import clap.server.adapter.inbound.web.dto.admin.response.MemberDetailsResponse;
import clap.server.adapter.inbound.web.dto.member.response.MemberDetailInfoResponse;
import clap.server.adapter.inbound.web.dto.member.response.MemberProfileResponse;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.domain.model.member.Member;

public class MemberResponseMapper {
    private MemberResponseMapper() {
        throw new IllegalStateException("Utility class");
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
                member.getMemberInfo().getEmail(),
                member.isReviewer(),
                member.getMemberInfo().getRole(),
                member.getMemberInfo().getDepartment().getName(),
                member.getMemberInfo().getDepartmentRole(),
                toNotificationSettingInfoResponse(member),
                member.getMemberInfo().getRole()!= MemberRole.ROLE_MANAGER ? null : toMemberRemainingTaskCountsResponse(member)
        );
    }

    public static MemberDetailInfoResponse.MemberRemainingTaskCountsResponse toMemberRemainingTaskCountsResponse(Member member){
        return new MemberDetailInfoResponse.MemberRemainingTaskCountsResponse(
                member.getInProgressTaskCount(),
                member.getInReviewingTaskCount()
        );
    }

    public static MemberDetailInfoResponse.NotificationSettingInfoResponse toNotificationSettingInfoResponse(Member member) {
        return new MemberDetailInfoResponse.NotificationSettingInfoResponse(
                member.getEmailNotificationEnabled(),
                member.getKakaoworkNotificationEnabled()
        );

    }

    public static MemberDetailsResponse toMemberDetailsResponse(Member member) {
        return new MemberDetailsResponse(
                member.getImageUrl(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getNickname(),
                member.getMemberInfo().getEmail(),
                member.isReviewer(),
                member.getMemberInfo().getRole(),
                member.getMemberInfo().getDepartment().getDepartmentId(),
                member.getMemberInfo().getDepartment().getName(),
                member.getMemberInfo().getDepartmentRole(),
                member.getStatus().equals(MemberStatus.PENDING) || member.getStatus().equals(MemberStatus.APPROVAL_REQUEST) ? null:
                member.getInProgressTaskCount() + member.getInReviewingTaskCount()
        );
    }

}