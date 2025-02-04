package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TeamTaskResponse;
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
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements CommandTaskPort, LoadTaskPort {
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
    public Page<Task> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findTasksRequestedByUser(requesterId, pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Page<Task> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findTasksAssignedByManager(processorId, pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Page<Task> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findPendingApprovalTasks(pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
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
    public Page<Task> findAllTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        return taskRepository.findAllTasks(pageable, filterTaskListRequest)
                .map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findPrevOrderTaskByProcessorIdAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder) {
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderLessThanOrderByProcessorOrderDesc(processorId, taskStatus, processorOrder);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Task> findNextOrderTaskByProcessorIdAndStatus(Long processorId, TaskStatus taskStatus, Long processorOrder) {
        Optional<TaskEntity> taskEntity = taskRepository.findTopByProcessor_MemberIdAndTaskStatusAndProcessorOrderAfterOrderByProcessorOrderDesc(processorId, taskStatus, processorOrder);
        return taskEntity.map(taskPersistenceMapper::toDomain);
    }

    @Override
    public Slice<Task> findTaskBoardByFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDate, FilterTaskBoardRequest request, Pageable pageable) {
        List<Task> taskList = new java.util.ArrayList<>(taskRepository.findTasksByFilter(processorId, statuses, untilDate, request, pageable)
                .stream()
                .map(taskPersistenceMapper::toDomain)
                .toList());

        boolean hasNext = taskList.size() > pageable.getPageSize();
        if (hasNext) {
            taskList.remove(taskList.size() - 1);
        }
        return new SliceImpl<>(taskList, pageable, hasNext);
    }

    @Override
    public List<TeamTaskResponse> findTeamStatus(Long memberId, FilterTeamStatusRequest filter) {
        return taskRepository.findTeamStatus(memberId, filter);
    }


}
