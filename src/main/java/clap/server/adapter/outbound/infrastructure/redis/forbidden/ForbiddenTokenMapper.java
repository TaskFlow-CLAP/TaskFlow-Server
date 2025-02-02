package clap.server.adapter.outbound.infrastructure.redis.forbidden;

import clap.server.domain.model.auth.ForbiddenToken;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ForbiddenTokenMapper {
    @InheritInverseConfiguration
    ForbiddenToken toDomain(final ForbiddenTokenEntity entity);

    ForbiddenTokenEntity toEntity(final ForbiddenToken domain);
}