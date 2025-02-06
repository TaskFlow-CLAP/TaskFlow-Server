package clap.server.adapter.inbound.web.dto.member.response;

import jakarta.validation.constraints.NotBlank;

public record SendVerificationCodeRequest(
        @NotBlank
        String nickname,
        @NotBlank
        String email
) {
}
