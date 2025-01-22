package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskCustomRepository {
    @Query("select t from TaskEntity t join fetch t.processor p" +
            " where t.updatedAt between :updatedAtAfter and :updatedAtBefore")
    List<TaskEntity> findYesterdayTaskByUpdatedAtIsBetween(
            @Param("updatedAtAfter") LocalDateTime updatedAtAfter,
            @Param("updatedAtBefore") LocalDateTime updatedAtBefore
    );
}