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

    @ManyToOne(/*optional = false, */fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")//, nullable = false)
    private MemberEntity member;

    @Override
    public ApiLog toDomain() {
        return toCommonDomainBuilder()
                .memberId(member != null ? String.valueOf(member.getMemberId()) : null) // memberId 포함
                .build();
    }
}
