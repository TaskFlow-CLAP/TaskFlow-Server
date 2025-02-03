package clap.server.adapter.inbound.web.dto.task.response;

import com.querydsl.core.annotations.QueryProjection;

import java.util.List;

public record TeamMemberTaskResponse(
        Long processorId,
        String nickname,
        String imageUrl,
        String department,
        int inProgressTaskCount,
        int pendingTaskCount,
        int totalTaskCount,
        List<TaskItemResponse> tasks
) {
    @QueryProjection
    public TeamMemberTaskResponse {
        tasks = (tasks == null) ? List.of() : tasks;
    }
}
