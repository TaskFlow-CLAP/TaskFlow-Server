package clap.server.adapter.inbound.web.dto.history.response;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskHistoryType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record FindTaskHistoryResponse(
        List<TaskHistoryResponse> histories
) {
    public static record TaskHistoryResponse(
            Long historyId,
            LocalDate date,
            LocalTime time,
            TaskHistoryType taskHistoryType,
            Details details
    ) {}

    public static record Details(
            TaskDetails taskDetails,
            CommentDetails commentDetails,
            CommentFileDetails commentFileDetails
    ) {}

    public static record TaskDetails(
            String value
    ) {}

    public static record CommentDetails(
            String nickName,
            String profileImageUrl,
            boolean isModified,
            String comment
    ) {}

    public static record CommentFileDetails(
            String nickName,
            String profileImageUrl,
            boolean isModified,
            String fileName,
            String url,
            String size
    ) {}
}
