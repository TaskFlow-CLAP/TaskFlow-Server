package clap.server.adapter.outbound.persistense.repository;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.domain.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task,Long> {
    List<TaskEntity> findYesterdayTaskByUpdatedAtIsBetween(LocalDateTime updatedAtAfter, LocalDateTime updatedAtBefore);
}
