package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.response.FilterAllTasksResponse;
import clap.server.adapter.inbound.web.dto.task.response.FilterAssignedTaskListResponse;
import clap.server.adapter.inbound.web.dto.task.response.FilterPendingApprovalResponse;
import clap.server.adapter.inbound.web.dto.task.response.FilterRequestedTasksResponse;
import org.springframework.data.domain.Pageable;

public interface FindTaskListUsecase {
    PageResponse<FilterRequestedTasksResponse> findTasksRequestedByUser(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    PageResponse<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    PageResponse<FilterPendingApprovalResponse> findPendingApprovalTasks(Long memberId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    PageResponse<FilterAllTasksResponse> findAllTasks(Long memberId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);
}
