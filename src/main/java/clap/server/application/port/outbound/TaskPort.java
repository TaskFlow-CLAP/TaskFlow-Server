package clap.server.application.port.outbound;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskPort {
    List<TaskEntity> findYesterdayTaskByDate(LocalDateTime now);
}
