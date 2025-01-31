package clap.server.adapter.inbound.web.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AnonymousLogResponse(
        @NotBlank
        Long logId,
        @NotBlank
        String loginNickname,
        @NotBlank
        LocalDateTime requestAt,
        @NotBlank
        LocalDateTime responseAt,
        @NotBlank
        String requestUrl,
        @NotBlank
        String requestMethod,
        @NotBlank
        Integer statusCode,
        @NotNull
        String customStatusCode,
        int failedAttempts
) {
}
