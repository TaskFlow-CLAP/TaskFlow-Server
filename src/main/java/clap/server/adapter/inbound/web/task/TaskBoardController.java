package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import clap.server.application.port.inbound.task.TaskBoardUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "02. Task [담당자]", description = " 작업 보드 API")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-board")
public class TaskBoardController {
    private final TaskBoardUsecase taskBoardUsecase;

    @GetMapping
    public ResponseEntity<TaskBoardResponse> getTaskBoard(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int pageSize,
                                                          @Parameter(description = "yyyy-mm-dd 형식으로 입력합니다.") @RequestParam(required = false)
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate untilDate,
                                                          @AuthenticationPrincipal SecurityUserDetails userInfo) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(taskBoardUsecase.getTaskBoards(userInfo.getUserId(), untilDate, pageable));
    }


}
