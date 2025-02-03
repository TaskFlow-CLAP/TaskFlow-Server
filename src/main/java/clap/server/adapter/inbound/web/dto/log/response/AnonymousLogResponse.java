package clap.server.adapter.inbound.web.dto.log.response;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record AnonymousLogResponse(
        @NotBlank
        Long logId,
        LogStatus logStatus,
        @NotBlank
        LocalDateTime requestAt,
        @NotBlank
        String nickName,
        String clientIp,
        @NotBlank
        Integer statusCode,
        int failedAttempts
) {
}
