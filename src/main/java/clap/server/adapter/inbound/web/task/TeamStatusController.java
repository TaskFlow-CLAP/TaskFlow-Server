package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;

import clap.server.adapter.inbound.web.dto.task.response.TeamStatusResponse;
import clap.server.application.port.inbound.task.FilterTeamStatusUsecase;
import clap.server.application.port.inbound.task.LoadTeamStatusUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-status")
@RequiredArgsConstructor
@Tag(name = "팀 현황 조회 API")
@WebAdapter
public class TeamStatusController {

    private final LoadTeamStatusUsecase loadTeamStatusUsecase;
    private final FilterTeamStatusUsecase filterTeamStatusUsecase;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<TeamStatusResponse> getTeamStatus(
            @PathVariable Long memberId,
            @RequestParam(defaultValue = "기본") String sortBy,
            @RequestParam(required = false) List<Long> mainCategoryIds,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String taskTitle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        FilterTeamStatusRequest filterRequest = new FilterTeamStatusRequest(sortBy, mainCategoryIds, categoryIds, taskTitle);
        Pageable pageable = PageRequest.of(page, pageSize);

        TeamStatusResponse response = loadTeamStatusUsecase.getTeamStatus(memberId, filterRequest, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<TeamStatusResponse> filterTeamStatus(
            @RequestParam(required = false) List<Long> mainCategoryIds,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) String taskTitle,
            @RequestParam(defaultValue = "기본") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize) {

        FilterTeamStatusRequest filterRequest = new FilterTeamStatusRequest(sortBy, mainCategoryIds, categoryIds, taskTitle);
        Pageable pageable = PageRequest.of(page, pageSize);

        TeamStatusResponse response = filterTeamStatusUsecase.filterTeamStatus(filterRequest, pageable);
        return ResponseEntity.ok(response);
    }
}

