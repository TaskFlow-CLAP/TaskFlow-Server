package clap.server.adapter.outbound.persistense;


import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;
import clap.server.adapter.outbound.persistense.mapper.TaskHistoryPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.history.TaskHistoryRepository;
import clap.server.application.port.outbound.taskhistory.CommandTaskHistoryPort;
import clap.server.application.port.outbound.taskhistory.LoadTaskHistoryPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.TaskHistory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@PersistenceAdapter
@RequiredArgsConstructor
public class TaskHistoryPersistenceAdapter implements LoadTaskHistoryPort, CommandTaskHistoryPort {
    private final TaskHistoryRepository taskHistoryRepository;
    private final TaskHistoryPersistenceMapper taskHistoryPersistenceMapper;

    @Override
    public List<TaskHistory> findAllTaskHistoriesByTaskId(final Long taskId) {
        return taskHistoryRepository.findAllTaskHistoriesByTaskId(taskId)
                .stream()
                .map(taskHistoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public TaskHistory save(final TaskHistory taskHistory) {
        TaskHistoryEntity taskHistoryEntity = taskHistoryPersistenceMapper.toEntity(taskHistory);
        TaskHistoryEntity savedTaskHistoryEntity = taskHistoryRepository.save(taskHistoryEntity);
        return taskHistoryPersistenceMapper.toDomain(savedTaskHistoryEntity);
    }

    @Override
    public void deleteTaskHistoryByCommentId(final Long commentId) {
        taskHistoryRepository.updateByComment_CommentId(commentId);
    }
}
