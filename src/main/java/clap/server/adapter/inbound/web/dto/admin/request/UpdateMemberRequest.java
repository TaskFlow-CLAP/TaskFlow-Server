package clap.server.adapter.inbound.web.dto.admin.request;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdateMemberRequest(
        @NotBlank @Schema(description = "회원 이름", example = "서주원")
        String name,
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
                message = "올바른 이메일 형식이 아닙니다.")
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

