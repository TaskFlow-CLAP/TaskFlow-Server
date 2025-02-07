package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamTaskResponse(
        Long processorId,
        String nickname,
        String imageUrl,
        String department,
        int inProgressTaskCount,
        int inReviewingTaskCount,
        int totalTaskCount,
        List<TeamTaskItemResponse> tasks
) {
}
