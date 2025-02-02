package clap.server.adapter.inbound.web.dto.task;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskProcessorRequest(

        @Schema(description = "변경할 담당자 고유 ID", example = "1")
        @NotBlank
        Long processorId
) {
}
