package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.CommentEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;

import clap.server.domain.model.task.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class, TaskPersistenceMapper.class})
public interface CommentPersistenceMapper  extends PersistenceMapper<CommentEntity, Comment> {
}
