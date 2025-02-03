package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskCustomRepository {

    Page<TaskEntity> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest);
    List<TeamMemberTaskResponse> findTeamStatus(Long memberId, FilterTeamStatusRequest filter);
    Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest findTaskListRequest);
    Page<TaskEntity> findAllTasks(Pageable pageable, FilterTaskListRequest findTaskListRequest);
    List<TaskEntity> findTasksByFilter(Long processorId, List<TaskStatus> statuses,  LocalDateTime localDateTime, FilterTaskBoardRequest request, Pageable pageable);
    Page<TaskEntity> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest findTaskListRequest);
}
