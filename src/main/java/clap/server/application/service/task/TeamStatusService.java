package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.outbound.persistense.TaskPersistenceAdapter;
import clap.server.adapter.outbound.persistense.repository.task.TaskCustomRepository;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.inbound.task.LoadTeamStatusUsecase;
import clap.server.application.port.outbound.task.LoadTaskPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamStatusService implements LoadTeamStatusUsecase, FilterTeamStatusUsecase {

    private final LoadTaskPort loadTaskPort; // LoadTaskPort를 통해 Repository 접근

    public TeamStatusService(LoadTaskPort loadTaskPort) {
        this.loadTaskPort = loadTaskPort;
    }

    @Override
    public TeamStatusResponse getTeamStatus(Long memberId, FilterTeamStatusRequest filter, Pageable pageable) {
        List<TeamMemberTaskResponse> members = loadTaskPort.findTeamStatus(memberId, filter); // 페이징 없음
        return new TeamStatusResponse(members);
    }

    @Override
    public TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter) {
        List<TeamMemberTaskResponse> members = loadTaskPort.findTeamStatus(null, filter);
        return new TeamStatusResponse(members);
    }

}
