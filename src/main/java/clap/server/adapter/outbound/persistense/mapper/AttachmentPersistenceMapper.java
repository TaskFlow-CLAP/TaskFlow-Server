package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.task.AttachmentEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.task.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskPersistenceMapper.class, CommentPersistenceMapper.class})
public interface AttachmentPersistenceMapper  extends PersistenceMapper<AttachmentEntity, Attachment> {
    @Override
    @Mapping(source = "deleted", target = "isDeleted")
    Attachment toDomain(final AttachmentEntity attachment);

    @Override
    @Mapping(source = "deleted", target = "isDeleted")
    AttachmentEntity toEntity(final Attachment Attachment);

}
