package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.application.port.inbound.task.CreateTaskUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class CreateTaskController {

    private final CreateTaskUsecase taskCreateUsecase;
    private static final Long memberId = 4L;

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest){
            return ResponseEntity.ok(taskCreateUsecase.createTask(memberId, createTaskRequest));
    }
}
