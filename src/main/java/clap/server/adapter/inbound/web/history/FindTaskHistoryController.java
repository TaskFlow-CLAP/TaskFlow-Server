package clap.server.adapter.inbound.web.history;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.history.response.FindTaskHistoryResponse;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.history.FindTaskHistoriesUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.log.LogType;
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


@Tag(name = "03. Task History")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class FindTaskHistoryController {

    private final FindTaskHistoriesUsecase findTaskHistoriesUsecase;

    @LogType(LogStatus.TASK_VIEWED)
    @Operation(summary = "작업 히스토리 조회")
    @Secured({"ROLE_MANAGER","ROLE_USER"})
    @GetMapping("/{taskId}/histories")
    public ResponseEntity<FindTaskHistoryResponse> findTaskHistories(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(findTaskHistoriesUsecase.findTaskHistories(userInfo.getUserId(), taskId));
    }
}
