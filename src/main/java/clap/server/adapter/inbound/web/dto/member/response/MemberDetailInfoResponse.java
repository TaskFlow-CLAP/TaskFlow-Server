package clap.server.adapter.inbound.web.dto.member.response;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberDetailInfoResponse(
        String profileImageUrl,
        @Schema(description = "회원 이름", example = "서주원")
        String name,
        @Schema(description = "회원 아이디", example = "siena.it")
        String nickname,
        @Schema(description = "회원 이메일", example = "siena.it@gmail.com")
        String email,
        @Schema(description = "승인 권한 여부")
        Boolean isReviewer,
        @Schema(description = "회원 역할")
        MemberRole role,
        @Schema(description = "부서")
        String departmentName,
        @Schema(description = "직책")
        String departmentRole,
        @Schema(description = "알림 수신 여부")
        NotificationSettingInfoResponse notificationSettingInfo,
        @Schema(description = "진행/검토 작업 수, 담당자가 아닐 경우에는 null입니다.")
        MemberRemainingTaskCountsResponse remainingTaskCounts
) {
    public static record NotificationSettingInfoResponse(
            @Schema(description = "이메일 알림 수신 여부")
            boolean email,
            @Schema(description = "카카오 워크 알림 수신 여부")
            boolean kakaoWork
    ) {
    }

    public static record MemberRemainingTaskCountsResponse(
            @Schema(description = "진행중 작업 수")
            int totalInProgressTaskCount,
            @Schema(description = "검토중 작업 수")
            int totalInReviewingTaskCount
    ) {
    }
}



