package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskItemResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.domain.model.task.Task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeamTaskResponseMapper {

    public static List<TeamTaskResponse> toTeamTaskResponses(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.groupingBy(t -> t.getProcessor().getMemberId(), LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(TeamTaskResponseMapper::toTeamTaskResponse)
                .collect(Collectors.toList());
    }

    private static TeamTaskResponse toTeamTaskResponse(Map.Entry<Long, List<Task>> entry) {
        List<TeamTaskItemResponse> taskResponses = entry.getValue().stream()
                .map(TeamTaskResponseMapper::toTeamTaskItemResponse)
                .collect(Collectors.toList());

        Task firstTask = entry.getValue().get(0);
        int inProgressTaskCount = firstTask.getProcessor().getInProgressTaskCount();
        int inReviewingTaskCount = firstTask.getProcessor().getInReviewingTaskCount();

        return new TeamTaskResponse(
                entry.getKey(),
                firstTask.getProcessor().getNickname(),
                firstTask.getProcessor().getImageUrl(),
                firstTask.getProcessor().getDepartment().getName(),
                inProgressTaskCount,
                inReviewingTaskCount,
                entry.getValue().size(),
                taskResponses
        );
    }

    private static TeamTaskItemResponse toTeamTaskItemResponse(Task task) {
        return new TeamTaskItemResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getTitle(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                toLabelInfo(task),
                task.getRequester().getNickname(),
                task.getRequester().getImageUrl(),
                task.getRequester().getDepartment().getName(),
                task.getProcessorOrder(),
                task.getTaskStatus(),
                task.getCreatedAt()
        );
    }

    private static TeamTaskItemResponse.LabelInfo toLabelInfo(Task task) {
        return task.getLabel() != null ?
                new TeamTaskItemResponse.LabelInfo(
                        task.getLabel().getLabelName(),
                        task.getLabel().getLabelColor()
                ) : null;
    }

    public static TeamStatusResponse toTeamStatusResponse(List<TeamTaskResponse> members, int totalInProgressTaskCount, int totalInReviewingTaskCount) {
        return new TeamStatusResponse(
                (members == null) ? List.of() : members,
                totalInProgressTaskCount,
                totalInReviewingTaskCount,
                totalInProgressTaskCount + totalInReviewingTaskCount
        );
    }
}
