package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.UpdateTaskOrderRequest;
import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.inbound.task.FilterTaskBoardUsecase;
import clap.server.application.port.inbound.task.UpdateTaskBoardUsecase;
import clap.server.application.port.inbound.task.UpdateTaskOrderAndStatusUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "02. Task [담당자]")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-board")
public class TaskBoardController {
    private final FilterTaskBoardUsecase filterTaskBoardUsecase;
    private final UpdateTaskBoardUsecase updateTaskBoardUsecase;
    private final UpdateTaskOrderAndStatusUsecase updateTaskOrderAndStatus;

    @Operation(summary = "작업 보드 조회 API")
    @Secured({"ROLE_MANAGER"})
    @GetMapping
    public ResponseEntity<TaskBoardResponse> getTaskBoard(
            @Parameter(description = "작업 완료 일자 조회 기준, yyyy-mm-dd 형식으로 입력합니다.") @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @ModelAttribute FilterTaskBoardRequest request,
            @AuthenticationPrincipal SecurityUserDetails userInfo) {
        return ResponseEntity.ok(filterTaskBoardUsecase.getTaskBoardByFilter(userInfo.getUserId(), fromDate, request));
    }

    @Operation(summary = "작업 보드 순서 및 상태 변경 API")
    @Secured({"ROLE_MANAGER"})
    @PatchMapping
    public void updateTaskBoard(@Parameter(description = "전환될 작업의 상태, 상태 전환이 아니라면 입력 X",
            schema = @Schema(allowableValues = {"IN_PROGRESS", "PENDING_COMPLETED", "COMPLETED"}))
                                @RequestParam(required = false) TaskStatus status,
                                @RequestBody UpdateTaskOrderRequest request,
                                @AuthenticationPrincipal SecurityUserDetails userInfo) {
        if (status == null) {
            updateTaskBoardUsecase.updateTaskOrder(userInfo.getUserId(), request);
        } else {
            updateTaskOrderAndStatus.updateTaskOrderAndStatus(userInfo.getUserId(), request, status);
        }
    }

}
