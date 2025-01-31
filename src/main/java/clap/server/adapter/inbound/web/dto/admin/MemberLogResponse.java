package clap.server.adapter.inbound.web.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MemberLogResponse(
        @NotBlank
        Long logId,
        @NotBlank
        Long memberId,
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
        String customStatusCode //TODO: 작업 구분 속성 추가
) {
}
