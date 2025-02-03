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
import org.springframework.data.domain.Page;
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
        Page<TeamMemberTaskResponse> members = loadTaskPort.findTeamStatus(memberId, filter, pageable); // 페이징 처리
        return new TeamStatusResponse(members.getContent()); // Page에서 List로 변환
    }

    @Override
    public TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter, Pageable pageable) {
        Page<TeamMemberTaskResponse> members = loadTaskPort.findTeamStatus(null, filter, pageable);
        return new TeamStatusResponse(members.getContent());
    }

}
