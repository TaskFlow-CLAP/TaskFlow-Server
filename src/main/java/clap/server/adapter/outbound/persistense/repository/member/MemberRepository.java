package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberCustomRepository {
    @Query("SELECT m FROM MemberEntity m LEFT JOIN FETCH m.department WHERE m.memberId = :id")
    Optional<MemberEntity> findByIdWithFetchDepartment(Long id);

    @Query("SELECT m FROM MemberEntity m LEFT JOIN FETCH m.department WHERE m.memberId = :id AND m.status='ACTIVE'")
    Optional<MemberEntity> findActiveMemberByIdWithFetchDepartment(Long id);

    List<MemberEntity> findByRoleAndStatus(MemberRole role, MemberStatus status);

    Optional<MemberEntity> findByStatusAndMemberId(MemberStatus memberStatus, Long memberId);

    @Query("SELECT m FROM MemberEntity m WHERE m.nickname = :nickname AND m.status = 'ACTIVE'")
    Optional<MemberEntity> findActiveMemberByNickname(@Param("nickname") String nickname);

    @Query("SELECT m FROM MemberEntity m WHERE m.nickname = :nickname AND m.status = 'APPROVAL_REQUEST'")
    Optional<MemberEntity> findApprovalRequestMemberByNickname(@Param("nickname") String nickname);

    List<MemberEntity> findByIsReviewerTrue();

    @Query(value = "SELECT DISTINCT m FROM MemberEntity m LEFT JOIN FETCH m.department",
            countQuery = "SELECT COUNT(DISTINCT m) FROM MemberEntity m")
    Page<MemberEntity> findAllMembersWithFetchDepartment(Pageable pageable);

    Optional<MemberEntity> findByMemberIdAndIsReviewerTrue(Long memberId);

    Optional<MemberEntity> findByNicknameAndEmail(String nickname, String email);

    Optional<MemberEntity> findByNameAndEmail(String name, String email);

    Optional<MemberEntity> findByEmail(String email);

    @Query("SELECT COUNT(m) > 0 FROM MemberEntity m WHERE m.status <> 'DELETED' AND (m.nickname IN :nicknames OR m.email IN :emails)")
    boolean existsByNicknamesOrEmails(@Param("nicknames") Set<String> nicknames, @Param("emails") Set<String> emails);
}

