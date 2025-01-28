package clap.server.adapter.inbound.web.dto.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

import java.time.LocalDateTime;

public record FilterAllTasksResponse(
        Long taskId,
        String taskCode,
        LocalDateTime requestedAt,
        String mainCategoryName,
        String categoryName,
        String title,
        String processorName,
        String requesterName,
        TaskStatus taskStatus,
        LocalDateTime finishedAt
) {
}
