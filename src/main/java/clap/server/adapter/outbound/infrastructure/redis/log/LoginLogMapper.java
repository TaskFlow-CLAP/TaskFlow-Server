package clap.server.adapter.outbound.infrastructure.redis.log;

import clap.server.domain.model.auth.LoginLog;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginLogMapper {

    @Mapping(target = "isLocked", source = "locked")
    LoginLog toDomain(final LoginLogEntity entity);

    @Mapping(target = "isLocked", source = "locked")
    LoginLogEntity toEntity(final LoginLog domain);

    default boolean mapLocked(boolean locked) {
        return locked;
    }
}
