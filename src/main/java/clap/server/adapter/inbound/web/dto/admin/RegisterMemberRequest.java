package clap.server.adapter.inbound.web.dto.admin;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterMemberRequest(
        @NotBlank @Schema(description = "회원 이름")
        String name,
        @NotBlank @Schema(description = "회원 이메일")
        String email,
        @NotBlank @Schema(description = "회원 닉네임, 로그인할 때 쓰입니다.") @Pattern(regexp = "^[a-z]{3,10}\\.[a-z]{1,5}$", message = "올바른 형식이 아닙니다.")
        String nickname,
        @NotNull @Schema(description = "승인 권한 여부")
        Boolean isReviewer,
        @NotNull @Schema(description = "부서 ID")
        Long departmentId,
        @NotNull @Schema(description = "회원 역할")
        MemberRole role,
        @NotBlank @Schema(description = "회원 직책")
        String departmentRole
) {
}

