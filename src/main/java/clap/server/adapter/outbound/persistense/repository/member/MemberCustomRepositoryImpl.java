package clap.server.adapter.outbound.persistense.repository.member;

import clap.server.adapter.inbound.web.dto.admin.request.FindMemberRequest;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.entity.member.constant.MemberStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static clap.server.adapter.outbound.persistense.entity.member.QMemberEntity.memberEntity;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;

    private Page<MemberEntity> executeQueryWithPageable(Pageable pageable, BooleanBuilder whereClause, OrderSpecifier<?> orderSpecifier) {
        List<MemberEntity> result = queryFactory
                .selectFrom(memberEntity)
                .where(whereClause)
                .orderBy(orderSpecifier) // 동적 sorting
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(memberEntity.count())
                .from(memberEntity)
                .where(whereClause)
                .fetchOne();

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

        if (!filterRequest.name().isBlank()) {
            whereClause.and(memberEntity.name.containsIgnoreCase(filterRequest.name()));
        }
        if (!filterRequest.email().isBlank()) {
            whereClause.and(memberEntity.email.containsIgnoreCase(filterRequest.email()));
        }
        if (!filterRequest.nickName().isBlank()) {
            whereClause.and(memberEntity.nickname.containsIgnoreCase(filterRequest.nickName()));
        }
        if (!filterRequest.departmentName().isBlank()) {
            whereClause.and(memberEntity.department.name.contains(filterRequest.departmentName()));
        }
        if (filterRequest.role() != null) {
            whereClause.and(memberEntity.role.eq(filterRequest.role()));
        }

        return whereClause;
    }

    @Override
    public Page<MemberEntity> findAllMembers(Pageable pageable) {
        OrderSpecifier<LocalDateTime> orderSpecifier = memberEntity.createdAt.desc(); // 기본 정렬: 최신순
        return executeQueryWithPageable(pageable, new BooleanBuilder().and(memberEntity.status.ne(MemberStatus.DELETED)), orderSpecifier);
    }

    @Override
    public Page<MemberEntity> findMembersWithFilter(Pageable pageable, FindMemberRequest filterRequest, String sortDirection) {
        BooleanBuilder whereClause = createMemberFilter(filterRequest);

        OrderSpecifier<LocalDateTime> orderSpecifier = sortDirection.equalsIgnoreCase("ASC")
                ? memberEntity.createdAt.asc() // ASC 정렬
                : memberEntity.createdAt.desc(); // DESC 정렬

        return executeQueryWithPageable(pageable, whereClause, orderSpecifier);
    }
}
