package clap.server.application.port.inbound.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindTaskListUsecase {
    Page<FindTaskListResponse> findRequestedTaskList(Long memberId, Pageable pageable, FilterTaskListRequest findTaskListRequest);
}
