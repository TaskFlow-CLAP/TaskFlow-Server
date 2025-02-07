package clap.server.application.port.outbound.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadTaskPort {
    Optional<Task> findById(Long id);

    List<Task> findYesterdayTaskByDate(LocalDateTime now);

    Page<Task> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Page<Task> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    Page<Task> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest);

    Page<Task> findAllTasks(Pageable pageable, FilterTaskListRequest findTaskListRequest);

    Optional<Task> findByIdAndStatus(Long id, TaskStatus status);

    Optional<Task> findPrevOrderTaskByProcessorOrderAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<Task> findNextOrderTaskByProcessorOrderAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<Task> findPrevOrderTaskByTaskIdAndStatus(Long processorId, TaskStatus taskStatus, Long taskId);

    Optional<Task> findNextOrderTaskByTaskIdAndStatus(Long processorId, TaskStatus taskStatus, Long taskId);

    List<Task> findTaskBoardByFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request);

    List<Task> findTeamStatus(Long memberId, FilterTeamStatusRequest filter);
}
