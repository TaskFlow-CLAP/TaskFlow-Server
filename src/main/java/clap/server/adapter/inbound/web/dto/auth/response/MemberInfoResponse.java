package clap.server.adapter.inbound.web.dto.auth.response;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponse(
        @Schema(description = "회원 ID")
        Long memberId,
        @Schema(description = "회원 이름")
        String memberName,
        @Schema(description = "회원 닉네임, 로그인에 쓰입니다")
        String nickname,
        @Schema(description = "회원 프로필 이미지")
        String imageUrl,
        @Schema(description = "회원 역할")
        MemberRole memberRole,
        @Schema(description = "회원 상태")
        MemberStatus memberStatus
) {}
