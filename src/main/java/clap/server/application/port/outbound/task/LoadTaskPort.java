package clap.server.application.port.outbound.task;


import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import clap.server.domain.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoadTaskPort {
    Optional<Task> findById(Long id);
    Page<FindTaskListResponse> findAllByRequesterId(Long requesterId, Pageable pageable, FindTaskListRequest findTaskListRequest);
}