package clap.server.adapter.outbound.infrastructure.redis.refresh;

import clap.server.domain.model.auth.RefreshToken;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    @InheritInverseConfiguration
    RefreshToken toDomain(final RefreshTokenEntity entity);

    RefreshTokenEntity toEntity(final RefreshToken domain);
}
