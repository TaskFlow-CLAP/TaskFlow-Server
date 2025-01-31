package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Attachment;

import java.util.List;
import java.util.Optional;


public interface LoadAttachmentPort {
    List<Attachment> findAllByTaskIdAndCommentIsNull(Long taskId);
    List<Attachment> findAllByTaskIdAndCommentIsNullAndAttachmentId(Long taskId, List<Long> attachmentIds);
    Optional<Attachment> findByCommentId(Long commentId);
    List<Attachment> findAllByTaskIdAndCommentIsNotNull(Long taskId);
    boolean exitsByCommentId(Long commentId);
}
