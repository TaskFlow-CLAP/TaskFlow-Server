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

    Optional<TaskEntity> findByTaskIdAndTaskStatus(Long id, TaskStatus status);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderLessThanOrderByProcessorOrderAsc(Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderAfterOrderByProcessorOrderAsc(
            Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndTaskIdLessThanOrderByTaskIdDesc(Long processorId, TaskStatus taskStatus, Long taskId);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndTaskIdGreaterThanOrderByTaskIdAsc(Long processorId, TaskStatus status, Long taskId);

    @Query("SELECT t FROM TaskEntity t " +
            "LEFT JOIN FETCH t.requester rq " +
            "LEFT JOIN FETCH t.processor p " +
            "LEFT JOIN FETCH p.department " +
            "WHERE t.taskId = :taskId")
    Optional<TaskEntity> findTaskWithProcessorDepartment(@Param("taskId") Long taskId);
 }