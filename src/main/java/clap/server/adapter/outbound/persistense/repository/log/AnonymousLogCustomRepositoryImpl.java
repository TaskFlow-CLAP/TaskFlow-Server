package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
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
import java.util.ArrayList;
import java.util.List;

import static clap.server.adapter.outbound.persistense.entity.log.QAnonymousLogEntity.anonymousLogEntity;
import static clap.server.adapter.outbound.persistense.entity.log.QMemberLogEntity.memberLogEntity;

@Repository
@RequiredArgsConstructor
public class AnonymousLogCustomRepositoryImpl implements AnonymousLogCustomRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<AnonymousLogEntity> filterAnonymousLogs(FilterLogRequest request, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            builder.and(anonymousLogEntity.requestAt.after(fromDate));
        }
        if (!request.logStatus().isEmpty()) {
            builder.and(anonymousLogEntity.logStatus.in(request.logStatus()));
        }
        if (!request.nickName().isEmpty()) {
            builder.and(anonymousLogEntity.loginNickname.contains(request.nickName()));
        }
        if (!request.clientIp().isEmpty()) {
            builder.and(anonymousLogEntity.clientIp.contains(request.clientIp()));
        }

        List<AnonymousLogEntity> result = queryFactory
                .selectFrom(anonymousLogEntity)
                .where(builder)
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .selectFrom(anonymousLogEntity)
                .where(builder)
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }

    // Pageable의 Sort 조건을 확인하여 동적으로 OrderSpecifier를 생성
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            // 정렬 조건이 없으면 requestAt 내림차순
            return new OrderSpecifier[]{ anonymousLogEntity.requestAt.desc() };
        }
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            if ("requestAt".equalsIgnoreCase(order.getProperty())) {
                orderSpecifiers.add(order.isAscending()
                        ? anonymousLogEntity.requestAt.asc()
                        : anonymousLogEntity.requestAt.desc());
            }
        });
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}
