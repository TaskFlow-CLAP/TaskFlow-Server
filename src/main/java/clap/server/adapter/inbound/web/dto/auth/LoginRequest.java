package clap.server.adapter.inbound.web.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String nickname,
        @NotNull
        String password
) {
}
