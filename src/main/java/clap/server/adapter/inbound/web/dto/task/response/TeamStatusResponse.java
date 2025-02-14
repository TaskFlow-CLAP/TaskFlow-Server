package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TeamStatusResponse(
        List<TeamTaskResponse> members,
        int totalInProgressTaskCount,
        int totalInReviewingTaskCount,
        int totalTaskCount
) {
}
