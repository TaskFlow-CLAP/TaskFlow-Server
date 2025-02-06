package clap.server.adapter.outbound.persistense.repository.history;

import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;

import java.util.List;

public interface TaskHistoryCustomRepository {
    List<TaskHistoryEntity> findAllTaskHistoriesByTaskId(Long taskId);
}
