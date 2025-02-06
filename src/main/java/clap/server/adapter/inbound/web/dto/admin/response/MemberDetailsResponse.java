package clap.server.adapter.inbound.web.dto.admin.response;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MemberDetailsResponse(
        String profileImageUrl,
        @Schema(description = "회원 이름", example = "서주원")
        String name,
        @Schema(description = "회원 아이디", example = "siena.it")
        String nicknanme,
        @Schema(description = "회원 이메일", example = "siena.it@gmail.com")
        String email,
        @Schema(description = "승인 권한 여부")
        Boolean isReviewer,
        @Schema(description = "회원 역할")
        MemberRole role,
        @NotNull @Schema(description = "부서 ID")
        Long departmentId,
        @Schema(description = "부서")
        String departmentName,
        @Schema(description = "직무")
        String departmentRole
) {}
  