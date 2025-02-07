package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "02. Task [담당자]")
@RequestMapping("/api/team-status")
@RequiredArgsConstructor
@WebAdapter
public class TeamStatusController {
    private final FilterTeamStatusUsecase filterTeamStatusUsecase;

    @Operation(summary = "팀 현황 조회 API")
    @GetMapping()
    @Secured("ROLE_MANAGER")
    public ResponseEntity<TeamStatusResponse> filterTeamStatus(@ModelAttribute @Valid FilterTeamStatusRequest filter) {
        TeamStatusResponse response = filterTeamStatusUsecase.filterTeamStatus(filter);
        return ResponseEntity.ok(response != null ? response : new TeamStatusResponse(List.of(), 0, 0, 0));
    }

}