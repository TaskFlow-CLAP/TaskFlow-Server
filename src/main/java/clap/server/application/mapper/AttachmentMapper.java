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

    public static List<Attachment> toCreateAttachments(Task task, List<String> fileUrls) {
        return fileUrls.stream()
                .map(fileUrl -> Attachment.builder()
                        .task(task)
                        .fileUrl(fileUrl)
                        .originalName("파일 이름")
                        .fileSize("16MB") //TODO: 하드코딩 제거
                        .build())
                .collect(Collectors.toList());
    }

    public static List<Attachment> toUpdateAttachments(Task task, List<AttachmentRequest> attachmentRequests) {
        return attachmentRequests.stream()
                .map(request -> Attachment.builder()
                        .task(task)
                        .fileUrl(request.fileUrl())
                        .originalName("수정된 파일 이름")
                        .fileSize("17MB")
                        .build())
                .collect(Collectors.toList());
    }


    public static List<Long> toAttachmentIds(List<AttachmentRequest> attachmentRequests) {
        return attachmentRequests.stream()
                .map(AttachmentRequest::fileId)
                .filter(Objects::nonNull)
                .toList();
    }

}

