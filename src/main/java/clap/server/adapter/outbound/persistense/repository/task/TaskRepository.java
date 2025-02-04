package clap.server.adapter.outbound.persistense.repository.task;


import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskCustomRepository {

    @Query("select t from TaskEntity t join fetch t.processor p" +
            " where t.updatedAt between :updatedAtAfter and :updatedAtBefore")

    List<TaskEntity> findYesterdayTaskByUpdatedAtIsBetween(
            @Param("updatedAtAfter") LocalDateTime updatedAtAfter,
            @Param("updatedAtBefore") LocalDateTime updatedAtBefore
    );


    List<TaskEntity> findByProcessor_MemberIdAndTaskStatusIn(Long memberId, Collection<TaskStatus> taskStatuses);


    @Query("SELECT t FROM TaskEntity t " +
            "WHERE t.processor.memberId = :processorId " +
            "AND t.taskStatus IN :taskStatus " +
            "AND (t.taskStatus != 'COMPLETED' OR t.finishedAt <= :untilDate) " +
            "ORDER BY t.processorOrder ASC ")
    Slice<TaskEntity> findTasksWithTaskStatusAndCompletedAt(
            @Param("processorId") Long processorId,
            @Param("taskStatus") List<TaskStatus> taskStatus,
            @Param("untilDate") LocalDateTime untilDate,
            Pageable pageable
    );

    Optional<TaskEntity> findByTaskIdAndTaskStatus(Long id, TaskStatus status);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderLessThanOrderByProcessorOrderDesc(Long processorId, TaskStatus taskStatus, Long processorOrder);

    Optional<TaskEntity> findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderAfterOrderByProcessorOrderDesc(
            Long processorId, TaskStatus taskStatus, Long processorOrder);

    @Query("SELECT t FROM TaskEntity t JOIN FETCH t.processor p WHERE (:memberId IS NULL OR p.memberId = :memberId) ")
    List<TeamMemberTaskResponse> findTeamStatus(@Param("memberId") Long memberId, FilterTeamStatusRequest filter);



}