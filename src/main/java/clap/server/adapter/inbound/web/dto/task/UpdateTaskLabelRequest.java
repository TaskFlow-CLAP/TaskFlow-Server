package clap.server.adapter.inbound.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskLabelRequest(

        @Schema(description = "변경할 구분 고유 ID", example = "1")
        @NotBlank
        Long labelId
) {
}
