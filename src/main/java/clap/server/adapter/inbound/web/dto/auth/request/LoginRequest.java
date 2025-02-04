package clap.server.adapter.inbound.web.dto.auth.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String nickname,
        @NotNull
        String password
) {
}
