package clap.server.domain.model.task;

import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    private boolean isDeleted;

    public static Attachment createAttachment(Task task, String originalName, String fileUrl, long fileSize) {
        return Attachment.builder()
                .task(task)
                .comment(null)
                .originalName(originalName)
                .fileUrl(fileUrl)
                .fileSize(formatFileSize(fileSize))
                .isDeleted(false)
                .build();
    }

    public static Attachment createCommentAttachment(Task task, Comment comment, String originalName, String fileUrl, long fileSize) {
        return Attachment.builder()
                .task(task)
                .comment(comment)
                .originalName(originalName)
                .fileUrl(fileUrl)
                .fileSize(formatFileSize(fileSize))
                .isDeleted(false)
                .build();
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        }
    }

}
