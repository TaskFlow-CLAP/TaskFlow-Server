package clap.server.adapter.outbound.persistense.repository.task;


import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskCustomRepository {

    @Query("select t from TaskEntity t left join fetch t.processor p" +
            " where t.updatedAt between :updatedAtAfter and :updatedAtBefore")
    List<TaskEntity> findYesterdayTaskByUpdatedAtIsBetween(
            @Param("updatedAtAfter") LocalDateTime updatedAtAfter,
            @Param("updatedAtBefore") LocalDateTime updatedAtBefore
    );


    List<TaskEntity> findByProcessor_MemberIdAndTaskStatusIn(Long memberId, Collection<TaskStatus> taskStatuses);


    @Query("SELECT t FROM TaskEntity t " +
            "WHERE t.processor.memberId = :processorId " +
            "AND t.taskStatus IN :taskStatus " +
            "AND (:fromDateTime IS NULL OR t.taskStatus != 'COMPLETED' OR " +
            "    (t.taskStatus = 'COMPLETED' AND t.finishedAt >= :fromDateTime)) " +
            "ORDER BY t.processorOrder ASC ")
    List<TaskEntity> findTasksWithTaskStatusAndCompletedAt(
            @Param("processorId") Long processorId,
            @Param("taskStatus") List<TaskStatus> taskStatus,
            @Param("fromDateTime") LocalDateTime fromDateTime
    );


    Optional<TaskEntity> findByTaskIdAndTaskStatus(Long id, TaskStatus status);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderLessThanOrderByProcessorOrderAsc(Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderAfterOrderByProcessorOrderAsc(
            Long processorId, TaskStatus taskStatus, Long processorOrder);

//    @Query("SELECT t FROM TaskEntity t JOIN FETCH t.processor p WHERE (:memberId IS NULL OR p.memberId = :memberId) ")
    List<TeamTaskResponse> findTeamStatus(@Param("memberId") Long memberId, FilterTeamStatusRequest filter);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndTaskIdLessThanOrderByTaskIdDesc(Long processorId, TaskStatus taskStatus, Long taskId);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndTaskIdGreaterThanOrderByTaskIdAsc(Long processorId, TaskStatus status, Long taskId);


}