package clap.server.adapter.outbound.persistense.mapper.common;

import clap.server.adapter.outbound.persistense.entity.common.BaseTimeEntity;
import clap.server.domain.model.common.BaseTime;
import org.mapstruct.InheritInverseConfiguration;

public interface PersistenceMapper<ENTITY extends BaseTimeEntity, DOMAIN extends BaseTime> {
    ENTITY toEntity(final DOMAIN domain);

    @InheritInverseConfiguration
    DOMAIN toDomain(final ENTITY entity);
}