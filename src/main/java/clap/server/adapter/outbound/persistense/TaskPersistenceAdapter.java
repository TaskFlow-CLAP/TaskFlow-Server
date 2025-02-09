package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements CommandTaskPort, LoadTaskPort {
    private final TaskRepository taskRepository;
    private final TaskPersistenceMapper taskPersistenceMapper;

    @Override
    public Task save(final Task task) {
        TaskEntity taskEntity = taskPersistenceMapper.toEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskPersistenceMapper.toDomain(savedTaskEntity);
    }

    @Override
    public Optional<Task> findById(final Long id) {
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Page<Task> findTasksRequestedByUser(final Long requesterId, final Pageable pageable, final FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findTasksRequestedByUser(requesterId, pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Page<Task> findTasksAssignedByManager(final Long processorId, final Pageable pageable, final FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findTasksAssignedByManager(processorId, pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Page<Task> findPendingApprovalTasks(final Pageable pageable, final FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findPendingApprovalTasks(pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findByIdAndStatus(final Long id, final TaskStatus status) {
        Optional<TaskEntity> taskEntity = taskRepository.findByTaskIdAndTaskStatus(id, status);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public List<Task> findYesterdayTaskByDate() {
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0, 0));
        return taskRepository.findYesterdayTaskByUpdatedAtIsBetween(now.minusDays(1), now)
                .stream().map(taskPersistenceMapper::toDomain).toList();
    }

    @Override
    public Page<Task> findAllTasks(final Pageable pageable, final FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findAllTasks(pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findPrevOrderTaskByProcessorOrderAndStatus(final Long processorId, final TaskStatus taskStatus, final Long processorOrder) {
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderLessThanOrderByProcessorOrderAsc(processorId, taskStatus, processorOrder);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findNextOrderTaskByProcessorOrderAndStatus(final Long processorId, final TaskStatus taskStatus, final Long processorOrder) {
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderAfterOrderByProcessorOrderAsc(processorId, taskStatus, processorOrder);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findPrevOrderTaskByTaskIdAndStatus(final Long processorId, final TaskStatus taskStatus, final Long taskId) {
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndTaskIdLessThanOrderByTaskIdDesc(processorId, taskStatus, taskId);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findNextOrderTaskByTaskIdAndStatus(final Long processorId, final TaskStatus taskStatus, final Long taskId) {
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndTaskIdGreaterThanOrderByTaskIdAsc(processorId, taskStatus, taskId);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public List<Task> findTaskBoardByFilter(final Long processorId, final List<TaskStatus> statuses, final LocalDateTime fromDate, final FilterTaskBoardRequest request) {
        return taskRepository.findTasksByFilter(processorId, statuses, fromDate, request)
                .stream()
                .map(taskPersistenceMapper::toDomain).toList();
    }

    @Override
    public List<Task> findTeamStatus(final Long memberId, final FilterTeamStatusRequest filter) {
        return taskRepository.findTeamStatus(memberId, filter).stream()
                .map(taskPersistenceMapper::toDomain).toList();
    }


}
