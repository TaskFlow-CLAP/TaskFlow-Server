package clap.server.adapter.outbound.persistense.repository;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.application.port.outbound.TaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskAdapter implements TaskPort {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public List<TaskEntity> findYesterdayTaskByDate(LocalDateTime now) {
        return taskJpaRepository.findYesterdayTaskByUpdatedAtIsBetween(now.minusDays(1), now);
    }
}
