package clap.server.adapter.outbound.persistense.entity.log;

import clap.server.domain.model.log.ApiLog;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("ANONYMOUS")
@SuperBuilder
public class AnonymousLogEntity extends ApiLogEntity {

    @Column(nullable = false)
    private String loginNickname;

//    @Override
//    public ApiLog toDomain() {
//        return toCommonDomainBuilder()
//                .userId(loginNickname)
//                .build();
//    }
}
