package clap.server.adapter.inbound.web.dto.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.LabelType;

import java.time.LocalDateTime;

public record ApprovalTaskResponse(
        Long taskId,
        String processorName,
        String reviewerName,
        LocalDateTime deadLine,
        LabelType labelName
) {
}
