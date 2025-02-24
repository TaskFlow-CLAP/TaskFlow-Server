package clap.server.application.port.outbound.member;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.domain.model.member.Member;

import java.util.List;
// Task 클래스 임포트 확인
// TaskStatus 임포트
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface LoadMemberPort {
    Optional<Member> findById(Long id);

    Optional<Member> findByIdWithFetchDepartment(Long id);

    Optional<Member> findActiveMemberById(Long id);

    Optional<Member> findActiveMemberByIdWithFetchDepartment(Long id);

    List<Member> findActiveManagers();

    Optional<Member> findReviewerById(Long id);

    Optional<Member> findActiveMemberByNickname(String nickname);

    Optional<Member> findApprovalMemberByNickname(String nickname);

    List<Member> findReviewers();

    Page<Member> findAllMembers(Pageable pageable);

    Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest, String sortDirection);

    Optional<Member> findByNicknameAndEmail(String nickname, String email);

    Optional<Member> findByNameAndEmail(String name, String email);

    Optional<Member> findByEmail(String email);

    boolean existsByNicknamesOrEmails(Set<String> nicknames, Set<String> emails);
}
