package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskCustomRepository {

    Page<TaskEntity> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest);
    Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest findTaskListRequest);
    Page<TaskEntity> findAllTasks(Pageable pageable, FilterTaskListRequest findTaskListRequest);
}
