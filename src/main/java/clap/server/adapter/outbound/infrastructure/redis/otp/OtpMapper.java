package clap.server.adapter.outbound.infrastructure.redis.otp;

import clap.server.domain.model.auth.Otp;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OtpMapper {
    @InheritInverseConfiguration
    Otp toDomain(final OtpEntity entity);

    OtpEntity toEntity(final Otp domain);
}