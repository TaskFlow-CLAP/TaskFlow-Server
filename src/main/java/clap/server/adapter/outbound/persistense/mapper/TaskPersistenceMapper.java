package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class, LabelPersistenceMapper.class, CategoryPersistenceMapper.class})
public interface TaskPersistenceMapper  extends PersistenceMapper<TaskEntity, Task> {

}
