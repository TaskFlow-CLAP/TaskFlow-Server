package clap.server.adapter.outbound.persistense.entity.log;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.domain.model.log.ApiLog;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("MEMBER")
@SuperBuilder
public class MemberLogEntity extends ApiLogEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private MemberEntity member;
}
