package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Attachment;

import java.util.List;


public interface LoadAttachmentPort {
    List<Attachment> findAllByTaskId(Long task);
}
