package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.CreateTaskRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.application.port.inbound.task.TaskUsecase;
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
public class TaskController {

    private final TaskUsecase taskUsecase;
    private static final Long memberId = 4L;

    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest){
            return ResponseEntity.ok(taskUsecase.createTask(memberId, createTaskRequest));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getRequestedTaskList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestBody FindTaskListRequest findTaskListRequest){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskUsecase.findRequestedTaskList(memberId, pageable, findTaskListRequest));
    }
}
