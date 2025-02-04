package clap.server.adapter.inbound.web.dto.task.response;

import clap.server.adapter.outbound.persistense.entity.task.constant.LabelColor;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.task.Label;

import java.time.LocalDateTime;

public record TaskItemResponse(
        Long taskId,
        String taskCode,
        String title,
        String mainCategoryName,
        String categoryName,
        LabelInfo labelInfo,
        String requesterNickname,
        String requesterImageUrl,
        String requesterDepartment,
        long processorOrder,
        TaskStatus taskStatus,
        LocalDateTime createdAt
) {
    public static record LabelInfo(
            String labelName,
            LabelColor labelColor
    ) {
    }

}

