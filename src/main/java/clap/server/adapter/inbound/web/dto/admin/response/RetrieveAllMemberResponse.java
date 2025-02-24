package clap.server.adapter.inbound.web.dto.admin.response;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RetrieveAllMemberResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "회원 이름", example = "양시훈")
        String name,

        @Schema(description = "회원 이메일", example = "sihun123@gmail.com")
        String email,

        @Schema(description = "회원 닉네임, 로그인할 때 쓰입니다.", example = "leo.sh")
        String nickname,

        @Schema(description = "승인 권한 여부", example = "true")
        Boolean isReviewer,

        @Schema(description = "부서 이름", example = "개발팀")
        String departmentName,

        @Schema(description = "회원 역할", example = "ROLE_USER")
        MemberRole role,

        @Schema(description = "회원 직책", example = "개발자")
        String departmentRole,

        @Schema(description = "가입일", example = "2024-01-01T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "회원 상태", example = "ACTIVE")
        MemberStatus memberStatus


) {}
