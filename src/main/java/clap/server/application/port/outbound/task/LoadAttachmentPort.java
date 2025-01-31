package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Attachment;

import java.util.List;


public interface LoadAttachmentPort {
    List<Attachment> findAllByTaskIdAndCommentIsNull(Long taskId);
    List<Attachment> findAllByTaskIdAndCommentIsNullAndAttachmentId(Long taskId, List<Long> attachmentIds);
    List<Attachment> findAllyByTaskIdAndCommentIdAndAttachmentId(Long taskId, Long commentId, List<Long> attachmentIds);
    List<Attachment> findAllByTaskIdAndCommentIsNotNull(Long taskId);
}
