package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.inbound.web.dto.admin.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static clap.server.adapter.outbound.persistense.entity.member.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    private Page<MemberEntity> executeQueryWithPageable(Pageable pageable, BooleanBuilder whereClause) {
        List<MemberEntity> result = queryFactory
                .selectFrom(memberEntity)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(memberEntity.count())
                .from(memberEntity)
                .where(whereClause)
                .fetch().size();

        return new PageImpl<>(
                result,
                pageable,
                total
        );
    }

    // 필터 조건 생성
    private BooleanBuilder createMemberFilter(FindMemberRequest filterRequest) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(memberEntity.status.ne(MemberStatus.DELETED));

        if (filterRequest.name() != null) {
            whereClause.and(memberEntity.name.containsIgnoreCase(filterRequest.name()));
        }
        if (filterRequest.email() != null) {
            whereClause.and(memberEntity.email.containsIgnoreCase(filterRequest.email()));
        }
        if (filterRequest.nickName() != null) {
            whereClause.and(memberEntity.nickname.containsIgnoreCase(filterRequest.nickName()));
        }
        if (filterRequest.departmentName() != null) {
            whereClause.and(memberEntity.department.name.eq(filterRequest.departmentName()));
        }
        if (filterRequest.role() != null) {
            whereClause.and(memberEntity.role.eq(filterRequest.role()));
        }

        return whereClause;
    }

    @Override
    public Page<MemberEntity> findAllMembers(Pageable pageable) {
        return executeQueryWithPageable(pageable, new BooleanBuilder().and(memberEntity.status.ne(MemberStatus.DELETED)));
    }

    @Override
    public Page<MemberEntity> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest) {
        BooleanBuilder whereClause = createMemberFilter(filterRequest);
        return executeQueryWithPageable(pageable, whereClause);
    }
}
