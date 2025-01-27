package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.application.port.inbound.task.ApprovalTaskUsecase;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.inbound.task.UpdateTaskStatusUsecase;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Tag(name = "02. Task", description = "작업 생성/수정 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ManagementTaskController {

    private final CreateTaskUsecase createTaskUsecase;
    private final UpdateTaskUsecase updateTaskUsecase;
    private final UpdateTaskStatusUsecase updateTaskStatusUsecase;
    private final ApprovalTaskUsecase approvalTaskUsecase;

    @Operation(summary = "작업 요청 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public ResponseEntity<CreateTaskResponse> createTask(
            @RequestPart(name = "taskInfo") @Valid CreateTaskRequest createTaskRequest,
            @RequestPart(name = "attachment") @NotNull  List<MultipartFile> attachments,
            @AuthenticationPrincipal SecurityUserDetails userInfo
            ){
            return ResponseEntity.ok(createTaskUsecase.createTask(userInfo.getUserId(), createTaskRequest, attachments));
    }

    @Operation(summary = "작업 수정")
    @PatchMapping(value = "/{taskId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public ResponseEntity<UpdateTaskResponse> updateTask(
            @PathVariable @NotNull Long taskId,
            @RequestPart(name = "taskInfo") @Valid UpdateTaskRequest updateTaskRequest,
            @RequestPart(name = "attachment") @NotNull  List<MultipartFile> attachments,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(updateTaskUsecase.updateTask(userInfo.getUserId(), taskId, updateTaskRequest, attachments));
    }

    @Operation(summary = "작업 상태 변경")
    @Secured({"ROLE_MANGER"})
    @PatchMapping("/state/{taskId}")
    public ResponseEntity<UpdateTaskResponse> updateTaskState(
            @PathVariable @NotNull Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo,
            @RequestBody UpdateTaskStateRequest updateTaskStateRequest) {

        return ResponseEntity.ok(updateTaskStatusUsecase.updateTaskState(userInfo.getUserId(), taskId, updateTaskStateRequest));
    }

    @Operation(summary = "작업 승인")
    @Secured({"ROLE_MANAGER"})
    @PostMapping("/approval/{taskId}")
    public ResponseEntity<ApprovalTaskResponse> approvalTask(
            @RequestBody @Valid ApprovalTaskRequest approvalTaskRequest,
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(approvalTaskUsecase.approvalTaskByReviewer(userInfo.getUserId(), taskId, approvalTaskRequest));
    }
}
