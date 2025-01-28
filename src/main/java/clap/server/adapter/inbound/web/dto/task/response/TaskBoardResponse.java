package clap.server.adapter.inbound.web.dto.task.response;

import java.util.List;

public record TaskBoardResponse(
        List<TaskItemResponse> tasksInProgress,
        List<TaskItemResponse> tasksPendingComplete,
        List<TaskItemResponse> tasksCompleted,
        boolean hasNext,
        boolean isFirst,
        boolean isLast
){
}
