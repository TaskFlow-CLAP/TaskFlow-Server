package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import clap.server.application.port.inbound.task.FindTaskListUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class FindTaskListController {
    private final FindTaskListUsecase taskListUsecase;
    private static final Long memberId = 4L;


    @GetMapping("/requests")
    public ResponseEntity<Page<FindTaskListResponse>> getRequestedTaskList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestBody FindTaskListRequest findTaskListRequest){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskListUsecase.findRequestedTaskList(memberId, pageable, findTaskListRequest));
    }
}
