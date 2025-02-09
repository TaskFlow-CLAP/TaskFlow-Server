package clap.server.adapter.inbound.web.dto.task.request;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull
        @Schema(examples = {"IN_PROGRESS", "IN_REVIEWING", "COMPLETED"})
        TaskStatus taskStatus
) {
}
