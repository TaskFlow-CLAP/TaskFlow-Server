package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.CommentEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Comment;
import clap.server.domain.model.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class, LabelPersistenceMapper.class, CategoryPersistenceMapper.class})
public interface TaskPersistenceMapper  extends PersistenceMapper<TaskEntity, Task> {
    @Override
    @Mapping(source = "deleted", target = "isDeleted")
    Task toDomain(final TaskEntity entity);

    @Override
    @Mapping(source = "deleted", target = "isDeleted")
    TaskEntity toEntity(final Task domain);
}
