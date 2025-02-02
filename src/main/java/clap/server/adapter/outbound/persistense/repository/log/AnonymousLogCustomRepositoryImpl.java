package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static clap.server.adapter.outbound.persistense.entity.log.QAnonymousLogEntity.anonymousLogEntity;

@Repository
@RequiredArgsConstructor
public class AnonymousLogCustomRepositoryImpl implements AnonymousLogCustomRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<AnonymousLogEntity> filterAnonymousLogs(FilterLogRequest request, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            builder.and(anonymousLogEntity.createdAt.after(fromDate));
        }
        if (!request.logStatus().isEmpty()) {
            builder.and(anonymousLogEntity.logStatus.in(request.logStatus()));
        }
        if (!request.nickName().isEmpty()) {
            builder.and(anonymousLogEntity.loginNickname.contains(request.nickName()));
        }
        if (!request.clientIp().isEmpty()) {
            builder.and(anonymousLogEntity.clientIp.eq(request.clientIp()));
        }

        List<AnonymousLogEntity> result = queryFactory
                .selectFrom(anonymousLogEntity)
                .where(builder)
                .orderBy(anonymousLogEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .selectFrom(anonymousLogEntity)
                .where(builder)
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }
}
