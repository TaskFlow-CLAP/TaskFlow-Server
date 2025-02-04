package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.inbound.task.LoadTeamStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamStatusService implements LoadTeamStatusUsecase, FilterTeamStatusUsecase {

    private final LoadTaskPort loadTaskPort;

    public TeamStatusService(LoadTaskPort loadTaskPort) {
        this.loadTaskPort = loadTaskPort;
    }

    @Override
    public TeamStatusResponse getTeamStatus(Long memberId, FilterTeamStatusRequest filter) {
        List<TeamMemberTaskResponse> members = loadTaskPort.findTeamStatus(memberId, filter);
        return new TeamStatusResponse(members);
    }

    @Override
    public TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter) {
        List<TeamMemberTaskResponse> members = loadTaskPort.findTeamStatus(null, filter);
        return new TeamStatusResponse(members);
    }

}
