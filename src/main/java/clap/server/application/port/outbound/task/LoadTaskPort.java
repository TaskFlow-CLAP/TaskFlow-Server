package clap.server.application.port.outbound.task;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import org.springframework.data.redis.stream.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadTaskPort {
    Optional<Task> findById(Long id);
    List<TaskEntity> findYesterdayTaskByDate(LocalDateTime now);
}