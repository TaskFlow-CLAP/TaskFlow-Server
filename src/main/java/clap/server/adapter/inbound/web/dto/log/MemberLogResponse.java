package clap.server.adapter.inbound.web.dto.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MemberLogResponse(
        @NotBlank
        Long logId,
        LogStatus logStatus,
        @NotBlank
        LocalDateTime requestAt,
        String nickName,
        String clientIp,
        @NotBlank
        Integer statusCode
) {
}
