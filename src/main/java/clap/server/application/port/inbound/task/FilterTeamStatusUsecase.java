package clap.server.application.port.inbound.task;


import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import org.springframework.data.domain.Pageable;

public interface FilterTeamStatusUsecase {
    TeamStatusResponse filterTeamStatus(FilterTeamStatusRequest filter);
}
