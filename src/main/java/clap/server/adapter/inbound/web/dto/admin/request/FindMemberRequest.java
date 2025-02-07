package clap.server.adapter.inbound.web.dto.admin.request;

import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record FindMemberRequest(
        @NotNull
        @Schema(description = "회원 이름", example = "양시훈")
        String name,

        @NotNull
        @Schema(description = "회원 이메일", example = "sihun123@gmail.com")
        String email,

        @NotNull
        @Schema(description = "회원 닉네임", example = "leo.sh")
        String nickName,

        @NotNull
        @Schema(description = "부서 이름", example = "1")
        String departmentName,

        @Schema(description = "회원 역할", example = "ROLE_USER")
        MemberRole role

) {}
