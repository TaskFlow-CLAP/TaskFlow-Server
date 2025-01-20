package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Attachment;

import java.util.Optional;

public interface CommandAttachmentPort {
    void save(Attachment attachment);
}
