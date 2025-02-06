package clap.server.adapter.outbound.persistense.repository.history;

import clap.server.adapter.outbound.persistense.entity.task.QCommentEntity;
import clap.server.adapter.outbound.persistense.entity.task.QTaskHistoryEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{
    private final JPAQueryFactory queryFactory;
    public void deleteCommentWithTaskHistory(Long commentId) {
        QTaskHistoryEntity taskHistory = QTaskHistoryEntity.taskHistoryEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;

        queryFactory
                .update(comment)
                .set(comment.isDeleted, true)
                .where(comment.commentId.eq(commentId))
                .execute();
       queryFactory
                .update(taskHistory)
                .set(taskHistory.isDeleted, true)
                .where(taskHistory.comment.commentId.eq(commentId))
                .execute();
    }
}
