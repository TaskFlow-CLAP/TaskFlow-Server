package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findByStatusAndMemberId(MemberStatus memberStatus, Long memberId);
}