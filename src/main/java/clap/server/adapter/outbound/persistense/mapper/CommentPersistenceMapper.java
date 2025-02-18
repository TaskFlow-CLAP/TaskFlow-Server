package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.CommentEntity;

import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;

import clap.server.domain.model.task.Comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class, TaskPersistenceMapper.class})
public interface CommentPersistenceMapper  extends PersistenceMapper<CommentEntity, Comment> {

    @Override
    @Mapping(source = "modified", target = "isModified")
    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(target = "task", ignore = true)
    Comment toDomain(final CommentEntity entity);

    @Override
    @Mapping(source = "modified", target = "isModified")
    @Mapping(source = "deleted", target = "isDeleted")
    CommentEntity toEntity(final Comment domain);
}
