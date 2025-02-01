package clap.server.application.service.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.adapter.outbound.persistense.repository.task.TaskCustomRepository;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.inbound.task.LoadTeamStatusUsecase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamStatusService implements LoadTeamStatusUsecase, FilterTeamStatusUsecase {

    private final TaskCustomRepository taskCustomRepository;

    public TeamStatusService(@Qualifier("taskCustomRepositoryImpl") TaskCustomRepository taskCustomRepository) {
        this.taskCustomRepository = taskCustomRepository;
    }

    @Override
    public TeamStatusResponse getTeamStatus(Long memberId, FilterTeamStatusRequest filter, Pageable pageable) {
        List<TeamMemberTaskResponse> members = taskCustomRepository.findTeamStatus(memberId, filter, pageable);
        return new TeamStatusResponse(members, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter, Pageable pageable) {
        List<TeamMemberTaskResponse> members = taskCustomRepository.findTeamStatus(null, filter, pageable);
        return new TeamStatusResponse(members, pageable.getPageNumber(), pageable.getPageSize());
    }

}
