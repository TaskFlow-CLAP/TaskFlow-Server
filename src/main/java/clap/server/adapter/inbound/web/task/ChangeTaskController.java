package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.request.ApprovalTaskRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskLabelRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskProcessorRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.ApprovalTaskResponse;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.application.port.inbound.task.UpdateTaskLabelUsecase;
import clap.server.application.port.inbound.task.UpdateTaskProcessorUsecase;
import clap.server.application.port.inbound.task.UpdateTaskStatusUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.log.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "02. Task [검토자]")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ChangeTaskController {

    private final UpdateTaskStatusUsecase updateTaskStatusUsecase;
    private final UpdateTaskProcessorUsecase updateTaskProcessorUsecase;
    private final UpdateTaskLabelUsecase updateTaskLabelUsecase;
    private final ApprovalTaskUsecase approvalTaskUsecase;

    @LogType(LogStatus.STATUS_CHANGED)
    @Operation(summary = "작업 상태 변경")
    @Secured("ROLE_MANAGER")
    @PatchMapping("/{taskId}/status")
    public void updateTaskState(
            @PathVariable @NotNull Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @Parameter(description = "변경하고 싶은 작업 상태",
                    schema = @Schema(allowableValues = {"IN_PROGRESS", "IN_REVIEWING", "COMPLETED"}))
            @RequestBody @Valid UpdateTaskStatusRequest request) {

        updateTaskStatusUsecase.updateTaskStatus(userInfo.getUserId(), taskId, request.taskStatus());
    }

    @LogType(LogStatus.ASSIGNER_CHANGED)
    @Operation(summary = "작업 담당자 변경")
    @Secured({"ROLE_MANAGER"})
    @PatchMapping("/{taskId}/processor")
    public void updateTaskProcessor(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @Valid @RequestBody UpdateTaskProcessorRequest updateTaskProcessorRequest) {
        updateTaskProcessorUsecase.updateTaskProcessor(taskId, userInfo.getUserId(), updateTaskProcessorRequest);
    }

    @Operation(summary = "작업 구분 변경")
    @Secured({"ROLE_MANAGER"})
    @PatchMapping("/{taskId}/label")
    public void updateTaskLabel(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @Valid @RequestBody UpdateTaskLabelRequest updateTaskLabelRequest) {
        updateTaskLabelUsecase.updateTaskLabel(taskId, userInfo.getUserId(), updateTaskLabelRequest);
    }
    @LogType(LogStatus.REQUEST_APPROVED)
    @Operation(summary = "작업 승인")
    @Secured({"ROLE_MANAGER"})
    @PostMapping("/{taskId}/approval")
    public ResponseEntity<ApprovalTaskResponse> approvalTask(
            @RequestBody @Valid ApprovalTaskRequest approvalTaskRequest,
            @PathVariable Long taskId,
            @Valid @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(approvalTaskUsecase.approvalTaskByReviewer(userInfo.getUserId(), taskId, approvalTaskRequest));
    }
}
