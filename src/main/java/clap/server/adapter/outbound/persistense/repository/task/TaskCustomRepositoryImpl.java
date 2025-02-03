package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
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
import static com.querydsl.core.types.Order.ASC;
import static com.querydsl.core.types.Order.DESC;

@Repository
@RequiredArgsConstructor
public class TaskCustomRepositoryImpl implements TaskCustomRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<TaskEntity> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.processor.nickname.contains(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.requester.memberId.eq(requesterId));

        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.requester.nickname.contains(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.processor.memberId.eq(processorId));

        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.requester.nickname.contains(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.taskStatus.eq(TaskStatus.REQUESTED));
        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findAllTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(
                    taskEntity.requester.nickname.contains(filterTaskListRequest.nickName())
                            .or(taskEntity.processor.nickname.contains(filterTaskListRequest.nickName()))
            );
        }
        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public List<TaskEntity> findTasksByFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request, Pageable pageable) {
        BooleanBuilder builder = createTaskBoardFilter(processorId, statuses, untilDateTime, request);
        return queryFactory
                .selectFrom(taskEntity)
                .where(builder)
                .orderBy(taskEntity.processorOrder.asc())
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();
    }

    private BooleanBuilder createTaskBoardFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(taskEntity.processor.memberId.eq(processorId));
        builder.and(taskEntity.taskStatus.in(statuses));
        builder.and(taskEntity.finishedAt.isNull().or(taskEntity.finishedAt.loe(untilDateTime)));

        if (request.labelId() != null) {
            builder.and(taskEntity.label.labelId.eq(request.labelId()));
        }
        if (request.mainCategoryId() != null) {
            builder.and(taskEntity.category.mainCategory.categoryId.eq(request.mainCategoryId()));
        }
        if (request.subCategoryId() != null) {
            builder.and(taskEntity.category.categoryId.eq(request.subCategoryId()));
        }
        if (request.title() != null && !request.title().isEmpty()) {
            String titleFilter = "%" + request.title() + "%";
            builder.and(taskEntity.title.like(titleFilter));
        }
        if (request.requesterNickname() != null && !request.requesterNickname().isEmpty()) {
            String nicknameFilter = "%" + request.requesterNickname().toLowerCase() + "%";
            builder.and(taskEntity.requester.nickname.lower().like(nicknameFilter));

        }
        return builder;
    }

    private BooleanBuilder createFilter(FilterTaskListRequest request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.term() != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusHours(request.term());
            builder.and(taskEntity.createdAt.after(fromDate));
        }
        if (!request.categoryIds().isEmpty()) {
            builder.and(taskEntity.category.categoryId.in(request.categoryIds()));
        }
        if (!request.mainCategoryIds().isEmpty()) {
            builder.and(taskEntity.category.mainCategory.categoryId.in(request.mainCategoryIds()));
        }
        if (!request.title().isEmpty()) {
            builder.and(taskEntity.title.containsIgnoreCase(request.title()));
        }
        if (!request.taskStatus().isEmpty()) {
            builder.and(taskEntity.taskStatus.in(request.taskStatus()));
        }
        return builder;
    }

    private Page<TaskEntity> getTasksPage(Pageable pageable, BooleanBuilder builder, String sortBy, String sortDirection) {
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortBy, sortDirection);

        List<TaskEntity> result = queryFactory
                .selectFrom(taskEntity)
                .leftJoin(taskEntity.processor).fetchJoin()
                .leftJoin(taskEntity.requester).fetchJoin()
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .selectFrom(taskEntity)
                .leftJoin(taskEntity.processor).fetchJoin()
                .leftJoin(taskEntity.requester).fetchJoin()
                .where(builder)
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