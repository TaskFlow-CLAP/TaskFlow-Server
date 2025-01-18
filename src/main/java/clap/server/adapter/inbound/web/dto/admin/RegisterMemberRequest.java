package clap.server.adapter.inbound.web.dto.admin;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterMemberRequest(
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String nickname,
        @NotNull
        Boolean isReviewer,
        @NotNull
        Long departmentId,
        @NotNull
        MemberRole role,
        @NotBlank
        String departmentRole,
        @NotNull
        Long adminId
) {
}

