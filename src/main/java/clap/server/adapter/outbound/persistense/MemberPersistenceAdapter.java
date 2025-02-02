package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import clap.server.adapter.outbound.persistense.mapper.MemberPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.member.MemberRepository;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.application.port.outbound.member.LoadMemberPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.task.Task;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus ;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.repository.task.TaskRepository;
import clap.server.adapter.outbound.persistense.mapper.TaskPersistenceMapper;

import java.util.stream.Collectors;
import java.util.List;

import clap.server.domain.model.member.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static clap.server.adapter.outbound.persistense.entity.member.QMemberEntity.memberEntity;

@PersistenceAdapter
@RequiredArgsConstructor
    public class MemberPersistenceAdapter implements LoadMemberPort, CommandMemberPort {
    private final MemberRepository memberRepository;
    private final MemberPersistenceMapper memberPersistenceMapper;
    private final TaskRepository taskRepository;
    private final TaskPersistenceMapper taskPersistenceMapper;
    private final JPAQueryFactory jpaQueryFactory;


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
        return executeQueryWithPageable(pageable, new BooleanBuilder().and(memberEntity.status.ne(MemberStatus.DELETED)));
    }

    @Override
    public Page<Member> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest) {
        BooleanBuilder whereClause = createMemberFilter(filterRequest);
        return executeQueryWithPageable(pageable, whereClause);
    }

    // 공통 쿼리 처리
    private Page<Member> executeQueryWithPageable(Pageable pageable, BooleanBuilder whereClause) {
        List<MemberEntity> entities = jpaQueryFactory
                .selectFrom(memberEntity)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(memberEntity.count())
                .from(memberEntity)
                .where(whereClause)
                .fetchOne();

        return new PageImpl<>(
                entities.stream()
                        .map(memberPersistenceMapper::toDomain)
                        .toList(),
                pageable,
                total
        );
    }

    // 필터 조건 생성
    private BooleanBuilder createMemberFilter(FindMemberRequest filterRequest) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(memberEntity.status.ne(MemberStatus.DELETED));

        if (filterRequest.getName() != null) {
            whereClause.and(memberEntity.name.containsIgnoreCase(filterRequest.getName()));
        }
        if (filterRequest.getEmail() != null) {
            whereClause.and(memberEntity.email.containsIgnoreCase(filterRequest.getEmail()));
        }
        if (filterRequest.getNickName() != null) {
            whereClause.and(memberEntity.nickname.containsIgnoreCase(filterRequest.getNickName()));
        }
        if (filterRequest.getDepartmentName() != null) {
            whereClause.and(memberEntity.department.name.containsIgnoreCase(filterRequest.getDepartmentName()));
        }
        if (filterRequest.getRole() != null) {
            whereClause.and(memberEntity.role.eq(filterRequest.getRole()));
        }

        return whereClause;
    }
}


