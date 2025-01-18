package clap.server.adapter.outbound.persistense.entity.log;

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
public class AnonymousLogEntity extends ApiLogEntity{
    @Column
    private String loginNickname;
}
