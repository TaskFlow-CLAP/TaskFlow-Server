package clap.server.adapter.inbound.web.dto.task.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TerminateTaskRequest(
        @Schema(description = "종료 사유")
        @NotBlank
        String reason
) {
}
