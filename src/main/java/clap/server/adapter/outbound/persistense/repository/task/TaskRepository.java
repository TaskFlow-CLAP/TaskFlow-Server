package clap.server.adapter.outbound.persistense.repository.task;


import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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


    @Query("SELECT t FROM TaskEntity t WHERE t.processor.memberId = :processorId " +
            "AND t.taskStatus IN :taskStatus " +
            "AND (t.taskStatus != 'COMPLETED' OR t.completedAt >= :untilDate)")
    Slice<TaskEntity> findTasksWithTaskStatusAndCompletedAt(
            @Param("processorId") Long processorId,
            @Param("taskStatus") List<TaskStatus> taskStatus,
            @Param("untilDate") LocalDateTime untilDate,
            Pageable pageable
    );


}








