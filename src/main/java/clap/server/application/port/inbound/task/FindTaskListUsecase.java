package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListResponse;
import clap.server.adapter.inbound.web.dto.task.FilterPendingApprovalResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindTaskListUsecase {
    Page<FilterTaskListResponse> findTasksRequestedByUser(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Page<FilterPendingApprovalResponse> findPendingApprovalTasks(Long userId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);
}
