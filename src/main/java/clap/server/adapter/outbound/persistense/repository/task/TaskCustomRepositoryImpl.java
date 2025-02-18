package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
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
            builder.and(taskEntity.processor.nickname.startsWith(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.requester.memberId.eq(requesterId));

        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.requester.nickname.startsWith(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.processor.memberId.eq(processorId));

        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public List<TaskEntity> findTeamStatus(Long memberId, FilterTeamStatusRequest filter) {
        BooleanBuilder builder = createFilterBuilder(memberId, filter);
        return queryFactory
                .select(taskEntity)
                .from(taskEntity)
                .leftJoin(taskEntity.requester).fetchJoin()
                .leftJoin(taskEntity.processor).fetchJoin()
                .join(taskEntity.requester.department).fetchJoin()
                .join(taskEntity.processor.department).fetchJoin()
                .where(builder)
                .fetch();
    }

    private BooleanBuilder createFilterBuilder(Long memberId, FilterTeamStatusRequest filter) {
        BooleanBuilder builder = new BooleanBuilder();

        // 필터가 null인 경우, 기본적으로 모든 데이터 조회
        if (filter != null) {
            // 진행 중 또는 완료 대기 상태 필터링
            builder.and(taskEntity.taskStatus.in(TaskStatus.IN_PROGRESS, TaskStatus.IN_REVIEWING));

            // 담당자 ID 필터링
            if (memberId != null) {
                builder.and(taskEntity.processor.memberId.eq(memberId));
            }

            // 작업 타이틀 필터링
            if (filter.taskTitle() != null && !filter.taskTitle().isEmpty()) {
                builder.and(taskEntity.title.containsIgnoreCase(filter.taskTitle()));
            }

            // 1차 카테고리 필터링 (빈 배열인 경우, 필터링하지 않음)
            if (filter.mainCategoryIds() != null && !filter.mainCategoryIds().isEmpty()) {
                builder.and(taskEntity.category.mainCategory.categoryId.in(filter.mainCategoryIds()));
            }

            // 2차 카테고리 필터링 (빈 배열인 경우, 필터링하지 않음)
            if (filter.categoryIds() != null && !filter.categoryIds().isEmpty()) {
                builder.and(taskEntity.category.categoryId.in(filter.categoryIds()));
            }
        }
        return builder;
    }

    @Override
    public Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.requester.nickname.startsWith(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.taskStatus.eq(TaskStatus.REQUESTED));
        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findAllTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(
                    taskEntity.requester.nickname.startsWith(filterTaskListRequest.nickName())
                            .or(taskEntity.processor.nickname.startsWith(filterTaskListRequest.nickName()))
            );
        }
        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public List<TaskEntity> findTasksByFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime untilDateTime, FilterTaskBoardRequest request) {
        BooleanBuilder builder = createTaskBoardFilter(processorId, statuses, untilDateTime, request);
        return queryFactory
                .selectFrom(taskEntity)
                .leftJoin(taskEntity.requester).fetchJoin()
                .leftJoin(taskEntity.requester.department).fetchJoin()
                .where(builder)
                .orderBy(taskEntity.processorOrder.asc())
                .fetch();
    }

    private BooleanBuilder createTaskBoardFilter(Long processorId, List<TaskStatus> statuses, LocalDateTime fromDateTime, FilterTaskBoardRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(taskEntity.processor.memberId.eq(processorId));
        builder.and(taskEntity.taskStatus.in(statuses));

        if (fromDateTime != null) {
            builder.and(taskEntity.taskStatus.ne(TaskStatus.COMPLETED)
                    .or(taskEntity.taskStatus.eq(TaskStatus.COMPLETED)
                            .and(taskEntity.finishedAt.goe(fromDateTime))));
        }

        if (request.labelId() != null) {
            builder.and(taskEntity.label.labelId.eq(request.labelId()));
        }
        if (!request.mainCategoryIds().isEmpty()) {
            builder.and(taskEntity.category.mainCategory.categoryId.in(request.mainCategoryIds())
                    .and(taskEntity.category.mainCategory.isDeleted.eq(false)));
        }
        if (!request.categoryIds().isEmpty()) {
            builder.and(taskEntity.category.categoryId.in(request.categoryIds())
                    .and(taskEntity.category.isDeleted.eq(false)));
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
                .select(taskEntity.count())
                .from(taskEntity)
                .leftJoin(taskEntity.processor)
                .leftJoin(taskEntity.requester)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortBy, String sortDirection) {
        DateTimePath<LocalDateTime> sortColumn = switch (sortBy) {
            case "REQUESTED_AT" -> taskEntity.createdAt;
            case "FINISHED_AT" -> taskEntity.finishedAt;
            default -> taskEntity.createdAt;
        };
        return "ASC".equalsIgnoreCase(sortDirection)
                ? new OrderSpecifier<>(ASC, sortColumn)
                : new OrderSpecifier<>(DESC, sortColumn);
    }
}