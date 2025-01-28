package clap.server.application.port.outbound.task;


import clap.server.domain.model.task.TaskHistory;

import java.util.List;

public interface LoadTaskHistoryPort {
    List<TaskHistory> findAllByTaskIdNotInComment(Long taskId);
    List<TaskHistory> findAllTaskHistoriesByTaskId(Long taskId);
}
