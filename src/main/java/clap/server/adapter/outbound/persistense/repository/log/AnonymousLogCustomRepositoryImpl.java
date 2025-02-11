package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
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

import static clap.server.adapter.outbound.persistense.entity.log.QAnonymousLogEntity.anonymousLogEntity;

@Repository
@RequiredArgsConstructor
public class AnonymousLogCustomRepositoryImpl implements AnonymousLogCustomRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<AnonymousLogEntity> filterAnonymousLogs(FilterLogRequest request, Pageable pageable, String sortDirection) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            builder.and(anonymousLogEntity.requestAt.after(fromDate));
        }
        if (!request.nickName().isEmpty()) {
            builder.and(anonymousLogEntity.loginNickname.contains(request.nickName()));
        }
        if (!request.clientIp().isEmpty()) {
            builder.and(anonymousLogEntity.clientIp.startsWith(request.clientIp()));
        }
        OrderSpecifier<LocalDateTime> orderSpecifier = sortDirection.equalsIgnoreCase("ASC")
                ? anonymousLogEntity.requestAt.asc()
                : anonymousLogEntity.requestAt.desc();

        List<AnonymousLogEntity> result = queryFactory
                .selectFrom(anonymousLogEntity)
                .where(builder)
                .orderBy(orderSpecifier)
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
