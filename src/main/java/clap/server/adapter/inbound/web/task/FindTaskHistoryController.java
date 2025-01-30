package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsForManagerResponse;
import clap.server.adapter.inbound.web.dto.task.response.FindTaskHistoryResponse;
import clap.server.application.port.inbound.task.FindTaskHistoriesUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "02. Task [조회]", description = "작업 조회 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class FindTaskHistoryController {

    private final FindTaskHistoriesUsecase findTaskHistoriesUsecase;

    @Operation(summary = "작업 히스토리 조회")
    @Secured({"ROLE_MANAGER","ROLE_USER"})
    @GetMapping("/histories/{taskId}")
    public ResponseEntity<FindTaskHistoryResponse> findTaskHistories(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(findTaskHistoriesUsecase.findTaskHistories(userInfo.getUserId(), taskId));
    }
}
