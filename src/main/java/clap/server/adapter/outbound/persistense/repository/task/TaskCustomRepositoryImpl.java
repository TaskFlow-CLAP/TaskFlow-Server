package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static clap.server.adapter.outbound.persistense.entity.task.QTaskEntity.taskEntity;
import static com.querydsl.core.types.Order.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TaskCustomRepositoryImpl implements TaskCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TaskEntity> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder whereClause = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            whereClause.and(taskEntity.processor.nickname.eq(filterTaskListRequest.nickName()));
        }
        whereClause.and(taskEntity.requester.memberId.eq(requesterId));

        return getTasksPage(pageable, whereClause, filterTaskListRequest.orderRequest().sortBy(), filterTaskListRequest.orderRequest().sortDirection());
    }

    @Override
    public Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder whereClause = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            whereClause.and(taskEntity.requester.nickname.eq(filterTaskListRequest.nickName()));
        }
        whereClause.and(taskEntity.taskStatus.eq(TaskStatus.REQUESTED));
        return getTasksPage(pageable, whereClause, filterTaskListRequest.orderRequest().sortBy(), filterTaskListRequest.orderRequest().sortDirection());
    }

    @Override
    public Page<TaskEntity> findAllTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder whereClause = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            whereClause.and(
                    taskEntity.requester.nickname.eq(filterTaskListRequest.nickName())
                            .or(taskEntity.processor.nickname.eq(filterTaskListRequest.nickName()))
            );
        }
        return getTasksPage(pageable, whereClause, filterTaskListRequest.orderRequest().sortBy(), filterTaskListRequest.orderRequest().sortDirection());
    }

    @Override
    public List<TaskEntity> findTasksByFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request, Pageable pageable) {
        BooleanBuilder whereClause = createTaskBoardFilter(processorId, statuses, untilDateTime, request);
        return queryFactory
                .selectFrom(taskEntity)
                .where(whereClause)
                .orderBy(taskEntity.processorOrder.asc())
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanBuilder createTaskBoardFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request) {
        BooleanBuilder whereClause = new BooleanBuilder();

        whereClause.and(taskEntity.processor.memberId.eq(processorId));
        whereClause.and(taskEntity.taskStatus.in(statuses));
        whereClause.and(taskEntity.finishedAt.isNull().or(taskEntity.finishedAt.loe(untilDateTime)));

        if (request.labelId() != null) {
            whereClause.and(taskEntity.label.labelId.eq(request.labelId()));
        }
        if (request.mainCategoryId() != null) {
            whereClause.and(taskEntity.category.mainCategory.categoryId.eq(request.mainCategoryId()));
        }
        if (request.subCategoryId() != null) {
            whereClause.and(taskEntity.category.categoryId.eq(request.subCategoryId()));
        }
        if (request.title() != null && !request.title().isEmpty()) {
            String titleFilter = "%" + request.title() + "%";
            whereClause.and(taskEntity.title.like(titleFilter));
        }
        if (request.requesterNickname() != null && !request.requesterNickname().isEmpty()) {
            String nicknameFilter = "%" + request.requesterNickname().toLowerCase() + "%";
            whereClause.and(taskEntity.requester.nickname.lower().like(nicknameFilter));

        }

        return whereClause;
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
            case "FINISHED_AT" -> taskEntity.finishedAt;
            default -> taskEntity.updatedAt;
        };
        return "ASC".equalsIgnoreCase(sortDirection)
                ? new OrderSpecifier<>(ASC, sortColumn)
                : new OrderSpecifier<>(DESC, sortColumn);
    }
}