package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.task.response.AttachmentResponse;
import clap.server.domain.model.task.Attachment;

import java.util.List;

public class AttachmentResponseMapper {
    private AttachmentResponseMapper() {
        throw new IllegalArgumentException("Utility class");
    }

    public static List<AttachmentResponse> toAttachmentResponseList(List<Attachment> attachments) {
        return attachments.stream()
                .map(attachment -> new AttachmentResponse(
                        attachment.getAttachmentId(),
                        attachment.getOriginalName(),
                        attachment.getFileSize(),
                        attachment.getFileUrl(),
                        attachment.getCreatedAt()
                ))
                .toList();
    }
}
