package clap.server.adapter.outbound.persistense.repository.history;

import clap.server.adapter.outbound.persistense.entity.member.QMemberEntity;
import clap.server.adapter.outbound.persistense.entity.task.QCommentEntity;
import clap.server.adapter.outbound.persistense.entity.task.QTaskHistoryEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


import java.util.List;

@RequiredArgsConstructor
public class TaskHistoryCustomRepositoryImpl implements TaskHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaskHistoryEntity> findAllTaskHistoriesByTaskId(Long taskId) {
        QTaskHistoryEntity taskHistory = QTaskHistoryEntity.taskHistoryEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        return queryFactory.selectFrom(taskHistory)
                .leftJoin(taskHistory.comment, comment).fetchJoin()
                .leftJoin(taskHistory.modifiedMember, member).fetchJoin()
                .where(
                        // Comment가 없는 경우에는 TaskModificationInfo의 Task 기준
                        taskHistory.comment.isNull()
                                .and(taskHistory.taskModificationInfo.task.taskId.eq(taskId))
                                .or(
                                        // Comment가 있는 경우에는 Comment의 Task 기준
                                        taskHistory.comment.isNotNull()
                                                .and(comment.task.taskId.eq(taskId))
                                )
                )
                .orderBy(taskHistory.taskHistoryId.asc()) // taskHistoryId로 정렬
                .fetch();
    }
}
