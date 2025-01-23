package clap.server.domain.model.task;

import clap.server.adapter.inbound.web.dto.task.AttachmentRequest;
import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment extends BaseTime {
    private Long attachmentId;
    private Task task;
    private Comment comment;
    private String originalName;
    private String fileUrl;
    private String fileSize;

    public static List<Attachment> createAttachments(Task task, List<String> fileUrls) {
        return fileUrls.stream()
                .map(fileUrl -> Attachment.builder()
                        .task(task)
                        .fileUrl(fileUrl)
                        .originalName("파일 이름")
                        .fileSize("16MB") //TODO: 하드코딩 제거
                        .build())
                .collect(Collectors.toList());
    }

    public static List<Attachment> updateAttachments(Task task, List<AttachmentRequest> attachmentRequests) {
        return attachmentRequests.stream()
                .map(request -> Attachment.builder()
                        .task(task)
                        .fileUrl(request.fileUrl())
                        .originalName("수정된 파일 이름")
                        .fileSize("17MB")
                        .build())
                .collect(Collectors.toList());
    }}
