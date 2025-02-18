package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.mapper.MemberPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.member.MemberRepository;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements LoadMemberPort, CommandMemberPort {
    private final MemberRepository memberRepository;
    private final MemberPersistenceMapper memberPersistenceMapper;

    @Override
    public Optional<Member> findById(final Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findById(id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findByIdWithFetchDepartment(Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findByIdWithFetchDepartment(id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findActiveMemberById(final Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findByStatusAndMemberId(MemberStatus.ACTIVE, id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findActiveMemberByIdWithFetchDepartment(Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findActiveMemberByIdWithFetchDepartment(id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findActiveMemberByNickname(final String nickname) {
        Optional<MemberEntity> memberEntity = memberRepository.findActiveMemberByNickname(nickname);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findApprovalMemberByNickname(String nickname) {
        Optional<MemberEntity> memberEntity = memberRepository.findApprovalRequestMemberByNickname(nickname);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public List<Member> findReviewers() {
        List<MemberEntity> memberEntities = memberRepository.findByIsReviewerTrue();
        return memberEntities.stream()
                .map(memberPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findReviewerById(final Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findByMemberIdAndIsReviewerTrue(id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public void save(final Member member) {
        MemberEntity memberEntity = memberPersistenceMapper.toEntity(member);
        memberRepository.save(memberEntity);
    }

    @Override
    public void saveAll(final List<Member> members) {
        List<MemberEntity> memberEntities = members.stream().map(memberPersistenceMapper::toEntity).toList();
        memberRepository.saveAll(memberEntities);
    }

    @Override
    public List<Member> findActiveManagers() {
        List<MemberEntity> memberEntities = memberRepository.findByRoleAndStatus(MemberRole.valueOf("ROLE_MANAGER"), MemberStatus.ACTIVE);
        return memberEntities.stream()
                .map(memberPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Member> findAllMembers(final Pageable pageable) {
        return memberRepository.findAllMembersWithFetchDepartment(pageable).map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Page<Member> findMembersWithFilter(final Pageable pageable, final FindMemberRequest filterRequest, final String sortDirection) {
        return memberRepository.findMembersWithFilter(pageable, filterRequest, sortDirection).map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findByNicknameAndEmail(final String nickname, final String email) {
        return memberRepository.findByNicknameAndEmail(nickname, email).map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findByNameAndEmail(final String name, final String email) {
        return memberRepository.findByNameAndEmail(name, email).map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .map(memberPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByNicknamesOrEmails(Set<String> nicknames, Set<String> emails) {
        return memberRepository.existsByNicknamesOrEmails(nicknames, emails);
    }
}