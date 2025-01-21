package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;

import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


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
    public Page<FindTaskListResponse> findAllByRequesterId(Long requesterId, Pageable pageable, FindTaskListRequest findTaskListRequest) {
        Page<TaskEntity> taskList = taskRepository.findRequestedTaskList(requesterId, pageable, findTaskListRequest);
        return taskList.map(this::convertToFindTaskListResponse);
    }

    private FindTaskListResponse convertToFindTaskListResponse(TaskEntity taskEntity) {
        return new FindTaskListResponse(
                taskEntity.getTaskId(),
                taskEntity.getTaskCode(),
                taskEntity.getCreatedAt(),
                taskEntity.getCategory().getMainCategory().getName(),
                taskEntity.getCategory().getName(),
                taskEntity.getTitle(),
                taskEntity.getProcessor() != null ? taskEntity.getProcessor().getName() : null,
                taskEntity.getStatus().getStatusName(),
                taskEntity.getCompletedAt()
        );
    }
}
