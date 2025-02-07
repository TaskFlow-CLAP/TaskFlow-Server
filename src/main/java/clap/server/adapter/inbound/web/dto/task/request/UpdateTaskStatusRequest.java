package clap.server.adapter.inbound.web.dto.task.request;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull
        TaskStatus taskStatus
) {
}
