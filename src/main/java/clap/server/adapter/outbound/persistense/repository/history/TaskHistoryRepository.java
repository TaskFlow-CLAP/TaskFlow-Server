package clap.server.adapter.outbound.persistense.repository.history;

import clap.server.adapter.outbound.persistense.entity.task.TaskHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Long>, TaskHistoryCustomRepository {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE TaskHistoryEntity t SET t.isDeleted = true WHERE t.comment.commentId = :commentId")
    void updateByComment_CommentId(@Param("commentId") Long commentId);
}
