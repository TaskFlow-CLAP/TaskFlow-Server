package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.application.port.inbound.task.UpdateTaskLabelUsecase;
import clap.server.application.port.inbound.task.UpdateTaskProcessorUsecase;
import clap.server.application.port.inbound.task.UpdateTaskStatusUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "02. Task[검토자]")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ChangeTaskController {

    private final UpdateTaskStatusUsecase updateTaskStatusUsecase;
    private final UpdateTaskProcessorUsecase updateTaskProcessorUsecase;
    private final UpdateTaskLabelUsecase updateTaskLabelUsecase;
    private final ApprovalTaskUsecase approvalTaskUsecase;

    @Operation(summary = "작업 상태 변경")
    @Secured("ROLE_MANAGER")
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<UpdateTaskResponse> updateTaskState(
            @PathVariable @NotNull Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody UpdateTaskStatusRequest updateTaskStatusRequest) {

        return ResponseEntity.ok(updateTaskStatusUsecase.updateTaskState(userInfo.getUserId(), taskId, updateTaskStatusRequest));
    }

    @Operation(summary = "작업 처리자 변경")
    @Secured({"ROLE_MANAGER"})
    @PatchMapping("/{taskId}/processor")
    public ResponseEntity<UpdateTaskResponse> updateTaskProcessor(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody UpdateTaskProcessorRequest updateTaskProcessorRequest) {
        return ResponseEntity.ok(updateTaskProcessorUsecase.updateTaskProcessor(taskId, userInfo.getUserId(), updateTaskProcessorRequest));
    }

    @Operation(summary = "작업 구분 변경")
    @Secured({"ROLE_MANAGER"})
    @PatchMapping("/{taskId}/label")
    public ResponseEntity<UpdateTaskResponse> updateTaskLabel(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody UpdateTaskLabelRequest updateTaskLabelRequest) {
        return ResponseEntity.ok(updateTaskLabelUsecase.updateTaskLabel(taskId, userInfo.getUserId(), updateTaskLabelRequest));
    }

    @Operation(summary = "작업 승인")
    @Secured({"ROLE_MANAGER"})
    @PostMapping("/{taskId}/approval")
    public ResponseEntity<ApprovalTaskResponse> approvalTask(
            @RequestBody @Valid ApprovalTaskRequest approvalTaskRequest,
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(approvalTaskUsecase.approvalTaskByReviewer(userInfo.getUserId(), taskId, approvalTaskRequest));
    }
}
