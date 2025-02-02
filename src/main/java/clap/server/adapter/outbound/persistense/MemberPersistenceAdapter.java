package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
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
    public Optional<Member> findActiveMemberById(final Long id) {
        Optional<MemberEntity> memberEntity = memberRepository.findByStatusAndMemberId(MemberStatus.ACTIVE, id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findByNickname(final String nickname) {
        Optional<MemberEntity> memberEntity = memberRepository.findByNickname(nickname);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    // 검토자인 담당자들 조회
    @Override
    public List<Member> findReviewers() {
        List<MemberEntity> memberEntities = memberRepository.findByIsReviewerTrue();
        return memberEntities.stream()
                .map(memberPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Member> findReviewerById(Long id) {
        Optional<MemberEntity> memberEntity =  memberRepository.findByMemberIdAndIsReviewerTrue(id);
        return memberEntity.map(memberPersistenceMapper::toDomain);
    }

    @Override
    public void save(final Member member) {
        MemberEntity memberEntity = memberPersistenceMapper.toEntity(member);
        memberRepository.save(memberEntity);
    }

    @Override
    public Page<Member> findAllMembers(Pageable pageable) {
        return memberRepository.findAllMembers(pageable).map(memberPersistenceMapper::toDomain);
    }

    @Override
    public Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest) {
        return memberRepository.findMembersWithFilter(pageable, filterRequest).map(memberPersistenceMapper::toDomain);
    }
}


