package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsResponse;
import clap.server.application.port.inbound.task.FindTaskDetailsUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class FindTaskDetailsController {
    private final FindTaskDetailsUsecase taskDetailsUsecase;
    private static final Long taskId = 3L;
    private static final Long memberId = 4L;

    @GetMapping("/requests/details")
    public ResponseEntity<List<FindTaskDetailsResponse>> getRequestedTaskDetails(){
        return ResponseEntity.ok(taskDetailsUsecase.findRequestedTaskDetails(memberId, taskId));
    }
}