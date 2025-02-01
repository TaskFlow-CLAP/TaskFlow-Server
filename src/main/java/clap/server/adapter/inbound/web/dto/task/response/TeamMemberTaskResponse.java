package clap.server.adapter.inbound.web.dto.task.response;

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
    public TeamMemberTaskResponse {
        tasks = (tasks == null) ? List.of() : tasks;
    }
}
