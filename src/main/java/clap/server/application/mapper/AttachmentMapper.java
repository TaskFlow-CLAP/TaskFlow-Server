package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.task.AttachmentRequest;
import clap.server.adapter.inbound.web.dto.task.AttachmentResponse;
import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsResponse;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AttachmentMapper {
    private AttachmentMapper() {
        throw new IllegalArgumentException();
    }



    public static List<Long> toAttachmentIds(List<AttachmentRequest> attachmentRequests) {
        return attachmentRequests.stream()
                .map(AttachmentRequest::fileId)
                .filter(Objects::nonNull)
                .toList();
    }

}

