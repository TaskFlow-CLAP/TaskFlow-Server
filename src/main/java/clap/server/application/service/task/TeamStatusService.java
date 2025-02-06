package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.inbound.task.LoadTeamStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
        return new TeamStatusResponse(members);
    }

}
