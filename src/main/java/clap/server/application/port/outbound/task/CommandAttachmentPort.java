package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Attachment;

import java.util.List;

public interface CommandAttachmentPort {
    void save(Attachment attachment);

    void saveAll(List<Attachment> attachments);
}
