package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskResponse;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "작업 생성 및 수정")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ManagementTaskController {

    private final CreateTaskUsecase createTaskUsecase;
    private final UpdateTaskUsecase updateTaskUsecase;

    @Operation(summary = "작업 요청 생성")
    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
            return ResponseEntity.ok(createTaskUsecase.createTask(userInfo.getUserId(), createTaskRequest));
    }

    @Operation(summary = "요청한 작업 수정")
    @PatchMapping
    public ResponseEntity<UpdateTaskResponse> updateTask(
            @RequestBody @Valid UpdateTaskRequest updateTaskRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(updateTaskUsecase.updateTask(userInfo.getUserId(), updateTaskRequest));
    }
}
