package clap.server.adapter.outbound.persistense.repository.task;
import clap.server.adapter.outbound.persistense.entity.task.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    List<AttachmentEntity> findAllByTask_TaskIdAndCommentIsNullAndDeletedIsFalse(Long taskId);
    List<AttachmentEntity> findAllByTask_TaskIdAndCommentIsNullAndDeletedIsFalseAndAttachmentIdIn(Long task_taskId, List<Long> attachmentId);

}