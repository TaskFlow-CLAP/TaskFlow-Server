package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static clap.server.adapter.outbound.persistense.entity.task.QTaskEntity.taskEntity;
import static com.querydsl.core.types.Order.*;

@Repository
@RequiredArgsConstructor
public class TaskCustomRepositoryImpl implements TaskCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TaskEntity> findRequestedTaskList(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(taskEntity.requester.memberId.eq(requesterId));

        List<Long> categoryIds = findTaskListRequest.categoryIds();
        List<Long> mainCategoryIds = findTaskListRequest.mainCategoryIds();
        String title = findTaskListRequest.title();
        String nickName = findTaskListRequest.nickName();
        List<TaskStatus> taskStatuses = findTaskListRequest.taskStatus();
        Integer termHours = findTaskListRequest.term();
        String sortTarget = findTaskListRequest.orderRequest().target();
        String sortType = findTaskListRequest.orderRequest().type();

        if (termHours != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(termHours);
            whereClause.and(taskEntity.createdAt.after(fromDate));
        }
        if (!categoryIds.isEmpty()) {
            whereClause.and(taskEntity.category.categoryId.in(categoryIds));
        }
        if (!mainCategoryIds.isEmpty()) {
            whereClause.and(taskEntity.category.mainCategory.categoryId.in(mainCategoryIds));
        }
        if (!title.isEmpty()) {
            whereClause.and(taskEntity.title.containsIgnoreCase(title));
        }
        if (!nickName.isEmpty()) {
            whereClause.and(taskEntity.processor.nickname.eq(nickName));
        }
        if (!taskStatuses.isEmpty()) {
            whereClause.and(taskEntity.taskStatus.in(taskStatuses));
        }

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortTarget, sortType);

        List<TaskEntity> result = queryFactory
                .selectFrom(taskEntity)
                .where(whereClause)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        int total = queryFactory
                .selectFrom(taskEntity)
                .where(whereClause)
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortTarget, String sortType) {
        DateTimePath<LocalDateTime> sortColumn = switch (sortTarget) {
            case "REQUESTED_AT" -> taskEntity.updatedAt;
            case "FINISHED_AT" -> taskEntity.completedAt;
            default -> taskEntity.updatedAt;
        };
        return "ASC".equalsIgnoreCase(sortType)
                ? new OrderSpecifier<>(ASC, sortColumn)
                : new OrderSpecifier<>(DESC, sortColumn);
    }
}
