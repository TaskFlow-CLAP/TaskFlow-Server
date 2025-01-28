package clap.server.adapter.inbound.web.dto.task.response;

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
            ProcessorChanged processorChanged,
            TaskStatusSwitched taskStatusSwitched,
            CommentDetails commentDetails,
            CommentFileDetails commentFileDetails
    ) {}

    public static record ProcessorChanged(
            String previousProcessor,
            String currentProcessor
    ) {}

    public static record TaskStatusSwitched(
            String taskStatus
    ) {}

    public static record CommentDetails(
            String name,
            String profileImageUrl,
            boolean isModified,
            String comment
    ) {}

    public static record CommentFileDetails(
            String name,
            String profileImageUrl,
            boolean isModified,
            String fileName,
            String url,
            String size
    ) {}
}
