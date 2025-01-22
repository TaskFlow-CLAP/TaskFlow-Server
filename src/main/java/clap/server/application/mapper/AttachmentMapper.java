package clap.server.application.mapper;

import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;

import java.util.List;
import java.util.stream.Collectors;

public class AttachmentMapper {
    private AttachmentMapper() {
        throw new IllegalArgumentException();
    }

    public static List<Attachment> toAttachments(Task task, List<String> fileUrls) {
        return fileUrls.stream()
                .map(fileUrl -> Attachment.builder()
                        .task(task)
                        .fileUrl(fileUrl)
                        .originalName("파일 예시 이름")
                        .fileSize("16MB") //TODO: 하드코딩 제거
                        .build())
                .collect(Collectors.toList());
    }
}

