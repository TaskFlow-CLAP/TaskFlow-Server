package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.CommentEntity;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Comment;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {MemberPersistenceMapper.class, TaskPersistenceMapper.class})
public abstract class CommentPersistenceMapper {

    @Autowired
    MemberPersistenceMapper memberPersistenceMapper;

    @Mapping(source = "modified", target = "isModified")
    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(target = "member", expression = "java(mapMember(entity))")
    @Mapping(target = "task", ignore = true)
    public abstract Comment toDomain(final CommentEntity entity);

    @Mapping(source = "modified", target = "isModified")
    @Mapping(source = "deleted", target = "isDeleted")
    public abstract CommentEntity toEntity(final Comment domain);

    protected Member mapMember(CommentEntity entity) {
        if (entity == null || entity.getMember() == null || !Hibernate.isInitialized(entity.getMember())) {
            return null;
        }
        return memberPersistenceMapper.toDomain(entity.getMember());
    }

}
