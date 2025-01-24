package clap.server.adapter.inbound.web.task;

import clap.server.adapter.inbound.security.SecurityUserDetails;
import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.application.port.inbound.task.FindTaskDetailsUsecase;
import clap.server.application.port.inbound.task.FindTaskListUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "작업 조회")
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class FindTaskController {
    private final FindTaskDetailsUsecase taskDetailsUsecase;
    private final FindTaskListUsecase taskListUsecase;

    @Operation(summary = "사용자 요청 작업 목록 조회")
    @Secured({"ROLE_USER"})
    @GetMapping("/requests")
    public ResponseEntity<Page<FilterTaskListResponse>> findTasksRequestedByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @ModelAttribute FilterTaskListRequest filterTaskListRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(taskListUsecase.findTasksRequestedByUser(userInfo.getUserId(), pageable, filterTaskListRequest));
    }
    @Operation(summary = "요청한 작업 상세 조회")
    @Secured({"ROLE_USER", "ROLE_MANAGER"})
    @GetMapping("/requests/details/{taskId}")
    public ResponseEntity<FindTaskDetailsResponse> findRequestedTaskDetails(
            @PathVariable Long taskId,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        return ResponseEntity.ok(taskDetailsUsecase.findRequestedTaskDetails(userInfo.getUserId(), taskId));
    }
    @Operation(summary = "승인대기 중인 요청 목록 조회")
    @Secured({"ROLE_MANAGER"})
    @GetMapping("/requests/pending")
    public ResponseEntity<Page<FilterPendingApprovalResponse>> findPendingApprovalTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @ModelAttribute FilterTaskListRequest filterTaskListRequest,
            @AuthenticationPrincipal SecurityUserDetails userInfo){
        Pageable pageable = PageRequest.of(page, pageSize);
        return ResponseEntity.ok(taskListUsecase.findPendingApprovalTasks(userInfo.getUserId(), pageable, filterTaskListRequest));
    }
}