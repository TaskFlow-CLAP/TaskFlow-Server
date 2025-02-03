package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.response.FindApprovalFormResponse;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "02. Task [조회]", description = "작업 조회 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class FindApprovalTaskFormController {

    private final ApprovalTaskUsecase approvalTaskUsecase;

    @Operation(summary = "요청 승인 폼 조회")
    @Secured("ROLE_MANAGER")
    @GetMapping("/approval/{taskId}")
    public ResponseEntity<FindApprovalFormResponse> findTaskForm(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(approvalTaskUsecase.findApprovalForm(userInfo.getUserId(), taskId));
    }
}
