package clap.server.adapter.inbound.web.dto.task.response;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

import java.time.LocalDateTime;

public record TaskItemResponse(
        Long taskId,
        String taskCode,
        String mainCategoryName,
        String categoryName,
        String requesterNickname,
        String requesterImageUrl,
        String requesterDepartment,
        long processorOrder,
        TaskStatus taskStatus,
        LocalDateTime createdAt
){
}
