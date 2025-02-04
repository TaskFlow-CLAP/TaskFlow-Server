package clap.server.application.port.outbound.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
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

    List<Task> findByProcessorAndStatus(Long processorId, List<TaskStatus> statuses, LocalDateTime fromDate);

    Optional<Task> findByIdAndStatus(Long id, TaskStatus status);

    Optional<Task> findPrevOrderTaskByProcessorIdAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<Task> findNextOrderTaskByProcessorIdAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder);

    List<Task> findTaskBoardByFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request);

    List<TeamMemberTaskResponse> findTeamStatus(Long memberId, FilterTeamStatusRequest filter);
}
