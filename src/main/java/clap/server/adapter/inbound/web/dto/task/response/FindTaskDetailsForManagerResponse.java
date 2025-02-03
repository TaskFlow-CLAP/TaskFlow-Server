package clap.server.adapter.inbound.web.dto.task.response;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record FindTaskDetailsForManagerResponse(
        Long taskId,
        String taskCode,
        LocalDateTime requestedAt,
        LocalDateTime finishedAt,
        TaskStatus taskStatus,
        String requesterNickName,
        String requesterImageUrl,
        String processorNickName,
        String processorImageUrl,
        String mainCategoryName,
        String categoryName,
        String title,
        String description,
        LocalDateTime dueDate,
        String labelName,
        List<AttachmentResponse> attachmentResponses
) implements TaskDetailsResponse {}
