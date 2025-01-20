package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.application.port.inbound.task.TaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskUsecase taskUsecase;

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest){

            final Long memberId = 1L;
            return ResponseEntity.ok(taskUsecase.createTask(memberId, createTaskRequest));
    }
}
