package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.application.port.outbound.task.LoadTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.stream.Task;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskPersistentAdapter implements LoadTaskPort {
    private final TaskRepository taskRepository;

    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TaskEntity> findYesterdayTaskByDate(LocalDateTime now) {

        return taskRepository.findYesterdayTaskByUpdatedAtIsBetween(now.minusDays(1), now);
    }
}
