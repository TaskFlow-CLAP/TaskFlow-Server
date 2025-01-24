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
    public Page<TaskEntity> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest findTaskListRequest) {
        BooleanBuilder whereClause = createFilter(findTaskListRequest);
        whereClause.and(taskEntity.requester.memberId.eq(requesterId));
        return getTasksPage(pageable, whereClause, findTaskListRequest.orderRequest().sortBy(), findTaskListRequest.orderRequest().sortDirection());
    }

    @Override
    public Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder whereClause = createFilter(filterTaskListRequest);
        whereClause.and(taskEntity.taskStatus.eq(TaskStatus.REQUESTED));
        return getTasksPage(pageable, whereClause, filterTaskListRequest.orderRequest().sortBy(), filterTaskListRequest.orderRequest().sortDirection());
    }

    private BooleanBuilder createFilter(FilterTaskListRequest request) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            whereClause.and(taskEntity.createdAt.after(fromDate));
        }
        if (!request.categoryIds().isEmpty()) {
            whereClause.and(taskEntity.category.categoryId.in(request.categoryIds()));
        }
        if (!request.mainCategoryIds().isEmpty()) {
            whereClause.and(taskEntity.category.mainCategory.categoryId.in(request.mainCategoryIds()));
        }
        if (!request.title().isEmpty()) {
            whereClause.and(taskEntity.title.containsIgnoreCase(request.title()));
        }
        if (!request.nickName().isEmpty()) {
            whereClause.and(taskEntity.processor.nickname.eq(request.nickName()));
        }
        if (!request.taskStatus().isEmpty()) {
            whereClause.and(taskEntity.taskStatus.in(request.taskStatus()));
        }
        return whereClause;
    }

    private Page<TaskEntity> getTasksPage(Pageable pageable, BooleanBuilder whereClause, String sortBy, String sortDirection) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, sortDirection);

        List<TaskEntity> result = queryFactory
                .selectFrom(taskEntity)
                .where(whereClause)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .selectFrom(taskEntity)
                .where(whereClause)
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String sortDirection) {
        DateTimePath<LocalDateTime> sortColumn = switch (sortBy) {
            case "REQUESTED_AT" -> taskEntity.updatedAt;
            case "FINISHED_AT" -> taskEntity.completedAt;
            default -> taskEntity.updatedAt;
        };
        return "ASC".equalsIgnoreCase(sortDirection)
                ? new OrderSpecifier<>(ASC, sortColumn)
                : new OrderSpecifier<>(DESC, sortColumn);
    }
}