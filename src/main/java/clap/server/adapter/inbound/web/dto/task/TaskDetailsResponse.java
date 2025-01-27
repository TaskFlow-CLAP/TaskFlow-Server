package clap.server.adapter.inbound.web.dto.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskDetailsResponse {
    Long taskId();
    String taskCode();
    LocalDateTime requestedAt();
    LocalDateTime finishedAt();
    TaskStatus taskStatus();
    String requesterNickName();
    String requesterImageUrl();
    String processorNickName();
    String processorImageUrl();
    String mainCategoryName();
    String categoryName();
    String title();
    String description();
    List<AttachmentResponse> attachmentResponses();
}
