package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;

import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.application.service.task.TeamStatusService;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "02. Task [담당자]")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team-status")
    public class TeamStatusController {
    
    private final TeamStatusService teamStatusService;
    @Operation(summary = "팀 현황 필터링 조회 API")
    @GetMapping("/filter")
    public ResponseEntity<TeamStatusResponse> filterTeamStatus(@ModelAttribute FilterTeamStatusRequest filter) {
        TeamStatusResponse response = teamStatusService.filterTeamStatus(filter);
        return ResponseEntity.ok(response != null ? response : new TeamStatusResponse(List.of(), 0, 0, 0));
    }
}
