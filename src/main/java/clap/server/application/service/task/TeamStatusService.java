package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.request.SortBy;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.application.mapper.response.TeamTaskResponseMapper;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class TeamStatusService implements FilterTeamStatusUsecase {

    private final LoadTaskPort loadTaskPort;

    @Override
    @Transactional(readOnly = true)
    public TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter) {
        List<Task> tasks = loadTaskPort.findTeamStatus(null, filter);

        List<TeamTaskResponse> taskItemResponses = TeamTaskResponseMapper.toTeamTaskResponses(tasks);

        int totalInProgressTaskCount = taskItemResponses.stream().mapToInt(TeamTaskResponse::inProgressTaskCount).sum();
        int totalInReviewingTaskCount = taskItemResponses.stream().mapToInt(TeamTaskResponse::inReviewingTaskCount).sum();

        if (filter.sortBy().equals(SortBy.CONTRIBUTE))
            taskItemResponses.sort((a, b) -> b.totalTaskCount() - a.totalTaskCount());
        else taskItemResponses.sort(Comparator.comparing(TeamTaskResponse::nickname));

        return TeamTaskResponseMapper.toTeamStatusResponse(taskItemResponses, totalInProgressTaskCount, totalInReviewingTaskCount);
    }


}
