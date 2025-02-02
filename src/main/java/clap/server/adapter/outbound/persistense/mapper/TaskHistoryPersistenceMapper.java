package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.TaskHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CommentPersistenceMapper.class, TaskPersistenceMapper.class, MemberPersistenceMapper.class})
public interface TaskHistoryPersistenceMapper extends PersistenceMapper<TaskHistoryEntity, TaskHistory> {
}
