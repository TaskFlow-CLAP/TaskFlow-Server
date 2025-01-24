package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListResponse;
import clap.server.adapter.inbound.web.dto.task.FilterTaskStatusRequestedListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindTaskListUsecase {
    Page<FilterTaskListResponse> findRequestedTaskList(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Page<FilterTaskStatusRequestedListResponse> findTaskListByTaskStatusRequested(Long userId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);
}
