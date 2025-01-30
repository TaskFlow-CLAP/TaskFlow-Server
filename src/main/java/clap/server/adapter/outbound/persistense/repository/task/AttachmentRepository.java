package clap.server.adapter.outbound.persistense.repository.task;
import clap.server.adapter.outbound.persistense.entity.task.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    List<AttachmentEntity> findAllByTask_TaskIdAndCommentIsNullAndIsDeletedIsFalse(Long taskId);
    List<AttachmentEntity> findAllByTask_TaskIdAndCommentIsNullAndIsDeletedIsFalseAndAttachmentIdIn(Long task_taskId, List<Long> attachmentId);
    List<AttachmentEntity> findActiveAttachmentsByTask_TaskIdAndComment_CommentIdAndAttachmentIdIn(Long task_taskId, Long comment_commentId, List<Long> attachmentIds);
    List<AttachmentEntity> findAllByTask_TaskIdAndCommentIsNotNullAndIsDeletedIsFalse(Long taskId);
}