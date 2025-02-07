package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>,  MemberCustomRepository {
    List<MemberEntity> findByRoleAndStatus(MemberRole role, MemberStatus status);

    Optional<MemberEntity> findByStatusAndMemberId(MemberStatus memberStatus, Long memberId);

    Optional<MemberEntity> findByNickname(String nickname);

    List<MemberEntity> findByIsReviewerTrue();

    List<MemberEntity> findAll(); // 전체 회원 조회

    Optional<MemberEntity> findByMemberIdAndIsReviewerTrue(Long memberId);

    Optional<MemberEntity> findByNicknameAndEmail(String nickname, String email);

    Optional<MemberEntity> findByNameAndEmail(String name, String email);

    Optional<MemberEntity> findByEmail(String email);
}

