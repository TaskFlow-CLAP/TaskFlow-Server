package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.task.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindTaskListUsecase {
    PageResponse<FilterRequestedTasksResponse> findTasksRequestedByUser(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    PageResponse<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    PageResponse<FilterPendingApprovalResponse> findPendingApprovalTasks(Long userId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    PageResponse<FilterAllTasksResponse> findAllTasks(Long userId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);
}
