package clap.server.adapter.inbound.web.dto.task.response;



import java.time.LocalDateTime;

public record FilterPendingApprovalResponse(
        Long taskId,
        String taskCode,
        LocalDateTime requestedAt,
        String mainCategoryName,
        String categoryName,
        String title,
        String requesterName,
        String requesterUrl
) {
}
