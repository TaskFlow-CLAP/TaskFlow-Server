package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.notification.NotificationEntity;
import clap.server.adapter.outbound.persistense.mapper.common.PersistenceMapper;
import clap.server.domain.model.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TaskPersistenceMapper.class, MemberPersistenceMapper.class})
public interface NotificationPersistenceMapper extends PersistenceMapper<NotificationEntity, Notification> {
    @Override
    @Mapping(source = "read", target = "isRead")
    Notification toDomain(final NotificationEntity entity);

    @Override
    @Mapping(source = "read", target = "isRead")
    NotificationEntity toEntity(final Notification notification);
}
