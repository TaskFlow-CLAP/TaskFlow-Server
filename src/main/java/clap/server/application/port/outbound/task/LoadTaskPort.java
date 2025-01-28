package clap.server.application.port.outbound.task;

import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.adapter.inbound.web.dto.task.FilterAllTasksResponse;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.adapter.inbound.web.dto.task.FilterRequestedTasksResponse;
import clap.server.domain.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadTaskPort {
    Optional<Task> findById(Long id);

    List<Task> findYesterdayTaskByDate(LocalDateTime now);

    Page<FilterRequestedTasksResponse> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Page<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    Page<FilterPendingApprovalResponse> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    Page<FilterAllTasksResponse> findAllTasks(Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Slice<Task> findByProcessorAndStatus(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDate, Pageable pageable);
}