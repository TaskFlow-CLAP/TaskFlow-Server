package clap.server.application.port.outbound.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import clap.server.domain.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadTaskPort {
    Optional<Task> findById(Long id);

    List<Task> findYesterdayTaskByDate(LocalDateTime now);

    Page<FindTaskListResponse> findAllByRequesterId(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

}