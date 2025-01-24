package clap.server.adapter.inbound.web.dto.task;



import java.time.LocalDateTime;

public record FilterTaskStatusRequestedListResponse(
        Long taskId,
        String taskCode,
        LocalDateTime requestedAt,
        String mainCategoryName,
        String categoryName,
        String title,
        String requesterName
) {
}
