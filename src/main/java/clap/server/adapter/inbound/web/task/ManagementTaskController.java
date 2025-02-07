package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.request.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.response.CreateTaskResponse;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.common.annotation.log.LogType;
import clap.server.exception.AdapterException;
import clap.server.exception.code.TaskErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
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

import static clap.server.domain.policy.task.TaskPolicyConstants.TASK_MAX_FILE_COUNT;


@Tag(name = "02. Task [생성/수정]", description = "작업 생성/수정 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ManagementTaskController {

    private final CreateTaskUsecase createTaskUsecase;
    private final UpdateTaskUsecase updateTaskUsecase;

    @LogType(LogStatus.REQUEST_CREATED)
    @Operation(summary = "작업 요청 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public ResponseEntity<CreateTaskResponse> createTask(
            @RequestPart(name = "taskInfo") @Valid CreateTaskRequest createTaskRequest,
            @Schema(description = "파일은 5개 이하만 업로드 가능합니다.")
            @RequestPart(name = "attachment", required = false) List<MultipartFile> attachments,
            @AuthenticationPrincipal SecurityUserDetails userInfo
    ) {
        if (attachments != null && attachments.size() > TASK_MAX_FILE_COUNT) {
            throw new AdapterException(TaskErrorCode.FILE_COUNT_EXCEEDED);
        }
        return ResponseEntity.ok(createTaskUsecase.createTask(userInfo.getUserId(), createTaskRequest, attachments));
    }
    @LogType(LogStatus.REQUEST_UPDATED)
    @Operation(summary = "작업 수정")
    @PatchMapping(value = "/{taskId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public void updateTask(
            @PathVariable @NotNull Long taskId,
            @RequestPart(name = "taskInfo") @Valid UpdateTaskRequest updateTaskRequest,
            @Schema(description = "하나의 작업에는 총 5개 이하만 업로드 가능합니다.")
            @RequestPart(name = "attachment", required = false) List<MultipartFile> attachments,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        if (attachments != null && attachments.size() > 5) {
            throw new AdapterException(TaskErrorCode.FILE_COUNT_EXCEEDED);
        }
        updateTaskUsecase.updateTask(userInfo.getUserId(), taskId, updateTaskRequest, attachments);
    }
}
