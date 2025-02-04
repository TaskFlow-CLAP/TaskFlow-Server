package clap.server.adapter.outbound.persistense.repository.log;


import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
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

import static clap.server.adapter.outbound.persistense.entity.log.QMemberLogEntity.memberLogEntity;
import static clap.server.adapter.outbound.persistense.entity.member.QMemberEntity.memberEntity;


@Repository
@RequiredArgsConstructor
public class MemberLogCustomRepositoryImpl implements MemberLogCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<MemberLogEntity> filterMemberLogs(FilterLogRequest request, Pageable pageable, String sortDirection) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            builder.and(memberLogEntity.requestAt.after(fromDate));
        }
        if (!request.logStatus().isEmpty()) {
            builder.and(memberLogEntity.logStatus.in(request.logStatus()));
        }
        if (!request.nickName().isEmpty()) {
            builder.and(memberEntity.nickname.contains(request.nickName()));
        }
        if (!request.clientIp().isEmpty()) {
            builder.and(memberLogEntity.clientIp.contains(request.clientIp()));
        }
        OrderSpecifier<LocalDateTime> orderSpecifier = sortDirection.equalsIgnoreCase("ASC")
                ? memberLogEntity.requestAt.asc()
                : memberLogEntity.requestAt.desc();

        List<MemberLogEntity> result = queryFactory
                .selectFrom(memberLogEntity)
                .where(builder)
                .leftJoin(memberLogEntity.member, memberEntity)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .selectFrom(memberLogEntity)
                .where(builder)
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }
}
