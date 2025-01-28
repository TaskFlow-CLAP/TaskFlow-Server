package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {


    List<MemberEntity> findByRoleAndStatus(MemberRole role, MemberStatus status);


    Optional<MemberEntity> findByStatusAndMemberId(MemberStatus memberStatus, Long memberId);

    Optional<MemberEntity> findByNickname(String nickname);

    List<MemberEntity> findByIsReviewerTrue();

}

