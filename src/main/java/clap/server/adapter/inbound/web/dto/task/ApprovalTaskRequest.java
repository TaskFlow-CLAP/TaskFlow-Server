package clap.server.adapter.inbound.web.dto.task;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ApprovalTaskRequest(
        @NotNull
        Long categoryId,
        @NotNull
        Long processorId,
        @NotNull
        LocalDateTime dueDate,
        @NotNull
        Long labelId

) {
}
