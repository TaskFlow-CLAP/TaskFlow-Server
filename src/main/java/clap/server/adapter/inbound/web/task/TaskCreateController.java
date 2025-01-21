package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.application.port.inbound.task.TaskCreateUsecase;
import clap.server.application.port.inbound.task.TaskListUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskCreateController {

    private final TaskCreateUsecase taskCreateUsecase;
    private static final Long memberId = 4L;

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest){
            return ResponseEntity.ok(taskCreateUsecase.createTask(memberId, createTaskRequest));
    }
}
