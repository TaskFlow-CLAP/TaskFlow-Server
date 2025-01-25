package clap.server.adapter.inbound.web.member;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record RetrieveAllMemberResponse(
        @Schema(description = "회원 이름", example = "양시훈")
        String name,

        @Schema(description = "회원 이메일", example = "sihun123@gmail.com")
        String email,

        @Schema(description = "회원 닉네임, 로그인할 때 쓰입니다.", example = "leo.sh")
        String nickname,

        @Schema(description = "승인 권한 여부", example = "true")
        Boolean isReviewer,

        @Schema(description = "부서 ID", example = "1")
        Long departmentId,

        @Schema(description = "회원 역할", example = "ROLE_USER")
        MemberRole role,

        @Schema(description = "회원 직책", example = "개발자")
        String departmentRole
) {}
