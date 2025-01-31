package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindTaskListUsecase {
    Page<FilterRequestedTasksResponse> findTasksRequestedByUser(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Page<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Page<FilterPendingApprovalResponse> findPendingApprovalTasks(Long userId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    Page<FilterAllTasksResponse> findAllTasks(Long userId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);
}
