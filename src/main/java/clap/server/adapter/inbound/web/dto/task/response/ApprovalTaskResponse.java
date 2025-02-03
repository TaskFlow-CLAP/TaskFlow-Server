package clap.server.adapter.inbound.web.dto.task.response;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

import java.time.LocalDateTime;

public record ApprovalTaskResponse(
        Long taskId,
        String processorName,
        String reviewerName,
        LocalDateTime deadLine,
        String labelName,
        TaskStatus taskStatus
) {
}
