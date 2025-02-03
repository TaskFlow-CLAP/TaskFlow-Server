package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.FilterTaskListRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTaskBoardRequest;
import clap.server.adapter.inbound.web.dto.task.request.FilterTeamStatusRequest;
import clap.server.adapter.inbound.web.dto.task.response.TaskItemResponse;
import clap.server.adapter.inbound.web.dto.task.response.TeamMemberTaskResponse;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static clap.server.adapter.outbound.persistense.entity.task.QTaskEntity.taskEntity;
import static com.querydsl.core.types.Order.*;

@Repository
@RequiredArgsConstructor
public class TaskCustomRepositoryImpl implements TaskCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public Page<TaskEntity> findTasksRequestedByUser(Long requesterId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.processor.nickname.eq(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.requester.memberId.eq(requesterId));

        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findTasksAssignedByManager(Long processorId, Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.requester.nickname.eq(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.processor.memberId.eq(processorId));

        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public List<TeamMemberTaskResponse> findTeamStatus(Long memberId, FilterTeamStatusRequest filter) {
        // 1. 담당자 목록을 가져옴 (페이징 제거)
        List<Long> processorIds = queryFactory
                .select(taskEntity.processor.memberId)
                .from(taskEntity)
                .groupBy(taskEntity.processor.memberId)
                .orderBy("기여도순".equals(filter.sortBy()) ?
                        taskEntity.taskId.count().desc() :
                        taskEntity.processor.nickname.asc())
                .fetch();

        if (processorIds.isEmpty()) {
            return List.of(); // 결과가 없으면 빈 리스트 반환
        }

        // 2. 담당자별 작업 조회 (페이징 제거)
        List<TaskEntity> taskEntities = queryFactory
                .selectFrom(taskEntity)
                .where(taskEntity.processor.memberId.in(processorIds))
                .fetch();

        // 3. 담당자별 그룹핑
        return taskEntities.stream()
                .collect(Collectors.groupingBy(t -> t.getProcessor().getMemberId()))
                .entrySet().stream()
                .map(entry -> {
                    List<TaskItemResponse> taskResponses = entry.getValue().stream()
                            .map(taskEntity -> new TaskItemResponse(
                                    taskEntity.getTaskId(),
                                    taskEntity.getTaskCode(),
                                    taskEntity.getTitle(),
                                    taskEntity.getCategory().getMainCategory().getName(),
                                    taskEntity.getCategory().getName(),
                                    taskEntity.getRequester().getNickname(),
                                    taskEntity.getRequester().getImageUrl(),
                                    taskEntity.getRequester().getDepartment().getName(),
                                    taskEntity.getProcessorOrder(),
                                    taskEntity.getTaskStatus(),
                                    taskEntity.getCreatedAt()
                            )).collect(Collectors.toList());

                    return new TeamMemberTaskResponse(
                            entry.getKey(),
                            entry.getValue().get(0).getProcessor().getNickname(),
                            entry.getValue().get(0).getProcessor().getImageUrl(),
                            entry.getValue().get(0).getProcessor().getDepartment().getName(),
                            (int) entry.getValue().stream().filter(t -> t.getTaskStatus() == TaskStatus.IN_PROGRESS).count(),
                            (int) entry.getValue().stream().filter(t -> t.getTaskStatus() == TaskStatus.PENDING_COMPLETED).count(),
                            entry.getValue().size(),
                            taskResponses
                    );
                }).collect(Collectors.toList());
    }



    private String buildQueryString(FilterTeamStatusRequest filter) {
        StringBuilder queryStr = new StringBuilder("SELECT t FROM TaskEntity t " +
                "JOIN FETCH t.processor p " +
                "WHERE (:memberId IS NULL OR p.memberId = :memberId) ");

        if (!filter.taskTitle().isEmpty()) {
            queryStr.append("AND t.title LIKE :title ");
        }
        if (!filter.mainCategoryIds().isEmpty()) {
            queryStr.append("AND t.category.mainCategory.id IN :mainCategories ");
        }
        if (!filter.categoryIds().isEmpty()) {
            queryStr.append("AND t.category.id IN :categories ");
        }

        if ("기여도순".equals(filter.sortBy())) {
            queryStr.append("ORDER BY (SELECT COUNT(te) FROM TaskEntity te WHERE te.processor = p AND te.taskStatus IN ('IN_PROGRESS', 'PENDING_COMPLETED')) DESC");
        } else {
            queryStr.append("ORDER BY p.nickname ASC");
        }

        return queryStr.toString();
    }

    private boolean isValidTitle(FilterTeamStatusRequest filter) {
        return filter.taskTitle() != null && !filter.taskTitle().isEmpty();
    }

    private void setQueryParameters(TypedQuery<TaskEntity> query, FilterTeamStatusRequest filter) {
        if (isValidTitle(filter)) {
            query.setParameter("title", "%" + filter.taskTitle() + "%");
        }
        if (!filter.mainCategoryIds().isEmpty()) {
            query.setParameter("mainCategories", filter.mainCategoryIds());
        }
        if (!filter.categoryIds().isEmpty()) {
            query.setParameter("categories", filter.categoryIds());
        }
    }

    @Override
    public Page<TaskEntity> findPendingApprovalTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(taskEntity.requester.nickname.eq(filterTaskListRequest.nickName()));
        }
        builder.and(taskEntity.taskStatus.eq(TaskStatus.REQUESTED));
        return getTasksPage(pageable, builder, filterTaskListRequest.sortBy(), filterTaskListRequest.sortDirection());
    }

    @Override
    public Page<TaskEntity> findAllTasks(Pageable pageable, FilterTaskListRequest filterTaskListRequest) {
        BooleanBuilder builder = createFilter(filterTaskListRequest);
        if (!filterTaskListRequest.nickName().isEmpty()) {
            builder.and(
                    taskEntity.requester.nickname.eq(filterTaskListRequest.nickName())
                            .or(taskEntity.processor.nickname.eq(filterTaskListRequest.nickName()))
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
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory
                .selectFrom(taskEntity)
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