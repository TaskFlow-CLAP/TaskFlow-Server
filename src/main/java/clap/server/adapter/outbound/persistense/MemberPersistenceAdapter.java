package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberRole;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.adapter.outbound.persistense.mapper.MemberPersistenceMapper;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.member.MemberRepository;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Task;
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
    private final TaskRepository taskRepository;
    private final TaskPersistenceMapper taskPersistenceMapper;

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
    public List<Task> findTasksByMemberIdAndStatus(final Long memberId, final List<TaskStatus> taskStatuses) {
        List<TaskEntity> taskEntities = taskRepository.findByProcessor_MemberIdAndTaskStatusIn(memberId, taskStatuses);
        return taskEntities.stream()
                .map(taskPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Member> findAllMembers(final Pageable pageable) {
        return memberRepository.findAllMembers(pageable).map(memberPersistenceMapper::toDomain);
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

}