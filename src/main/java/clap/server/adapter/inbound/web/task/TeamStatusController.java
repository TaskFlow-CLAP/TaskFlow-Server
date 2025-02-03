package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;

import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.application.service.task.TeamStatusService;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team-status")
@RequiredArgsConstructor
@Tag(name = "팀 현황 조회 API")
@WebAdapter
public class TeamStatusController {

    private final TeamStatusService teamStatusService;

    @GetMapping("/filter")
    public ResponseEntity<TeamStatusResponse> filterTeamStatus(@ModelAttribute FilterTeamStatusRequest filter) {
        TeamStatusResponse response = teamStatusService.filterTeamStatus(filter);
        return ResponseEntity.ok(response);
    }
}