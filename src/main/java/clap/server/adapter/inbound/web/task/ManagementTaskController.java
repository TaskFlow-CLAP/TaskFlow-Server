package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.application.port.inbound.task.*;
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


@Tag(name = "02. Task [생성/수정]", description = "작업 생성/수정 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ManagementTaskController {

    private final CreateTaskUsecase createTaskUsecase;
    private final UpdateTaskUsecase updateTaskUsecase;

    @Operation(summary = "작업 요청 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    public ResponseEntity<CreateTaskResponse> createTask(
            @RequestPart(name = "taskInfo") @Valid CreateTaskRequest createTaskRequest,
            @RequestPart(name = "attachment", required = false) List<MultipartFile> attachments,
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
            @RequestPart(name = "attachment", required = false) List<MultipartFile> attachments,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(updateTaskUsecase.updateTask(userInfo.getUserId(), taskId, updateTaskRequest, attachments));
    }
}
