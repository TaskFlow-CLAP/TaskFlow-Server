package clap.server.adapter.inbound.web.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String password
) {
}
