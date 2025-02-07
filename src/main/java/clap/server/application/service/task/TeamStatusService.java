package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.inbound.task.LoadTeamStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
public class TeamStatusService implements LoadTeamStatusUsecase, FilterTeamStatusUsecase {

    private final LoadTaskPort loadTaskPort;

    public TeamStatusService(LoadTaskPort loadTaskPort) {
        this.loadTaskPort = loadTaskPort;
    }

    @Override
    public TeamStatusResponse getTeamStatus(Long memberId, FilterTeamStatusRequest filter) {
        List<TeamTaskResponse> members = loadTaskPort.findTeamStatus(memberId, filter);
        if (members == null) {
            members = List.of();
        }
        return new TeamStatusResponse(members);
    }

    @Override
    @Transactional(readOnly = true)
    public TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter) {
        List<TeamTaskResponse> members = loadTaskPort.findTeamStatus(null, filter);

        if (members == null) {
            members = List.of();
        }

        // 전체 팀의 진행 중 & 검토 중 작업 수 계산
        int totalInProgressTaskCount = members.stream().mapToInt(TeamTaskResponse::inProgressTaskCount).sum();
        int totalInReviewingTaskCount = members.stream().mapToInt(TeamTaskResponse::inReviewingTaskCount).sum();

        return new TeamStatusResponse(members, totalInProgressTaskCount, totalInReviewingTaskCount);
    }


}
