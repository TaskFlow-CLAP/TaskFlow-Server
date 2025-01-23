package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Attachment;

import java.util.List;
import java.util.Optional;

public interface CommandAttachmentPort {
    void save(Attachment attachment);

    void saveAll(List<Attachment> attachments);

    void deleteByIds(List<Long> attachmentId);
}
