package clap.server.adapter.inbound.web.dto.member.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyPasswordRequest(
        @NotBlank
        String password
) {
}
