package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.task.FilterAllTasksResponse;
import clap.server.adapter.inbound.web.dto.task.FilterPendingApprovalResponse;
import clap.server.adapter.inbound.web.dto.task.FilterRequestedTasksResponse;
import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.application.mapper.TaskMapper;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements CommandTaskPort , LoadTaskPort {
    private final TaskRepository taskRepository;
    private final TaskPersistenceMapper taskPersistenceMapper;

    @Override
    public Task save(Task task) {
        TaskEntity taskEntity = taskPersistenceMapper.toEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskPersistenceMapper.toDomain(savedTaskEntity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Page<FilterRequestedTasksResponse> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        Page<Task> taskList = taskRepository.findTasksRequestedByUser(requesterId, pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
        return taskList.map(TaskMapper::toFilterRequestedTasksResponse);
    }

    @Override
    public Page<FilterAssignedTaskListResponse> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        Page<Task> taskList = taskRepository.findTasksAssignedByManager(processorId, pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
        return taskList.map(TaskMapper::toFilterAssignedTaskListResponse);
    }

    @Override
    public Page<FilterPendingApprovalResponse> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        Page<Task> taskList = taskRepository.findPendingApprovalTasks(pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
        return taskList.map(TaskMapper::toFilterPendingApprovalTasksResponse);
    }

    @Override
    public Slice<Task> findByProcessorAndStatus(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDate, Pageable pageable) {
        Slice<TaskEntity> tasks = taskRepository.findTasksWithTaskStatusAndCompletedAt(processorId, statuses, untilDate, pageable);
        return tasks.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findByIdAndStatus(Long id, TaskStatus status) {
        Optional<TaskEntity> taskEntity = taskRepository.findByTaskIdAndTaskStatus(id, status);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public List<Task> findYesterdayTaskByDate(LocalDateTime now) {
        return taskRepository.findYesterdayTaskByUpdatedAtIsBetween(now.minusDays(1), now)
                .stream().map(taskPersistenceMapper::toDomain).toList();
    }

    @Override
    public Page<FilterAllTasksResponse> findAllTasks (Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        Page<Task> taskList = taskRepository.findAllTasks(pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
        return taskList.map(TaskMapper::toFilterAllTasksResponse);
    }
    @Override
    public Optional<Task> findPrevOrderTaskByProcessorIdAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder){
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderLessThanOrderByProcessorOrderDesc(processorId, taskStatus, processorOrder);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findNextOrderTaskByProcessorIdAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder){
        Optional<TaskEntity> taskEntity =  taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderAfterOrderByProcessorOrderDesc(processorId, taskStatus, processorOrder);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

}
