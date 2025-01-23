package clap.server.adapter.outbound.persistense.repository.task;

import clap.server.adapter.inbound.web.dto.task.FindTaskListRequest;
import clap.server.adapter.outbound.persistense.entity.task.QTaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.task.Term;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskCustomRepositoryImpl implements TaskCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TaskEntity> findRequestedTaskList(Long requesterId, Pageable pageable, FindTaskListRequest findTaskListRequest) {
        QTaskEntity task = QTaskEntity.taskEntity;
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(task.requester.memberId.eq(requesterId));

        List<Long> categoryIds = findTaskListRequest.categoryIds();
        List<Long> mainCategoryIds = findTaskListRequest.mainCategoryIds();
        String title = findTaskListRequest.title();
        String nickName = findTaskListRequest.nickName();
        TaskStatus taskStatus = findTaskListRequest.taskStatus();
        Term term = findTaskListRequest.term();

        if (term != null) {
            LocalDateTime fromDate = LocalDateTime.now().minusMonths(term.getHours());
            whereClause.and(task.createdAt.after(fromDate));
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            whereClause.and(task.category.categoryId.in(categoryIds));  // 리스트를 처리하는 in 조건
        }
        if (mainCategoryIds != null && !mainCategoryIds.isEmpty()) {
            whereClause.and(task.category.mainCategory.categoryId.in(mainCategoryIds));  // 리스트를 처리하는 in 조건
        }
        if (title != null && !title.isEmpty()) {
            whereClause.and(task.title.containsIgnoreCase(title));
        }
        if (nickName != null) {
            whereClause.and(task.processor.nickname.eq(nickName));
        }
        if (taskStatus != null) {
            whereClause.and(task.taskStatus.eq(taskStatus));
        }

        List<TaskEntity> result = queryFactory
                .selectFrom(task)
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = queryFactory
                .selectFrom(task)
                .where(whereClause)
                .fetch().size();

        return new PageImpl<>(result, pageable, total);
    }
}
