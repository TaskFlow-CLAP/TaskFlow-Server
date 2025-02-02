package clap.server.application.port.outbound.taskhistory;


import clap.server.domain.model.task.TaskHistory;

import java.util.List;

public interface LoadTaskHistoryPort {
    List<TaskHistory> findAllTaskHistoriesByTaskId(Long taskId);
}
