package clap.server.application.mapper;

import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;

public class AttachmentMapper {
    private AttachmentMapper() {
        throw new IllegalArgumentException();
    }

    public static Attachment toAttachment(Task task, String fileUrl) {
        return Attachment.builder()
                .task(task)
                .fileUrl(fileUrl)
                .originalName("파일 예시 이름")
                .fileSize("16MB") //TODO: 하드코딩 제거
                .build();
    }
}
