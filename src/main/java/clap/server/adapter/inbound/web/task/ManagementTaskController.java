package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.CreateAndUpdateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.UpdateTaskRequest;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.application.port.inbound.task.UpdateTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class ManagementTaskController {

    private final CreateTaskUsecase createTaskUsecase;
    private final UpdateTaskUsecase updateTaskUsecase;
    private static final Long memberId = 4L;

    @PostMapping
    public ResponseEntity<CreateAndUpdateTaskResponse> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest){
            return ResponseEntity.ok(createTaskUsecase.createTask(memberId, createTaskRequest));
    }

    @PatchMapping
    public ResponseEntity<CreateAndUpdateTaskResponse> updateTask(
            @RequestBody @Valid UpdateTaskRequest updateTaskRequest){
        return ResponseEntity.ok(updateTaskUsecase.updateTask(memberId, updateTaskRequest));
    }
}
