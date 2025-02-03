package clap.server.adapter.outbound.persistense.repository.log;


import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import com.querydsl.core.BooleanBuilder;
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
    public Page<MemberLogEntity> filterMemberLogs(FilterLogRequest request, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            builder.and(memberLogEntity.createdAt.after(fromDate));
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

        List<MemberLogEntity> result = queryFactory
                .selectFrom(memberLogEntity)
                .where(builder)
                .leftJoin(memberLogEntity.member, memberEntity)
                .orderBy(memberLogEntity.createdAt.desc())
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
