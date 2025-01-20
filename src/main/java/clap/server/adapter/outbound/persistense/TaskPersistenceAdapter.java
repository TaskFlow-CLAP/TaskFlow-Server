package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.application.port.outbound.task.CommandTaskPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;

import clap.server.domain.model.task.Task;
import lombok.RequiredArgsConstructor;



@PersistenceAdapter
@RequiredArgsConstructor
public class TaskPersistenceAdapter implements CommandTaskPort {
    private final TaskRepository taskRepository;
    private final TaskPersistenceMapper taskPersistenceMapper;
    @Override
    public Task save(Task task) {
        TaskEntity taskEntity = taskPersistenceMapper.toEntity(task);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        return taskPersistenceMapper.toDomain(savedTaskEntity);
    }
}
