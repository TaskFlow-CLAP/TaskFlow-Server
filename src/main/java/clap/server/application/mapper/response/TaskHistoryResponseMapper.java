package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.history.response.FindTaskHistoryResponse;

import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.TaskHistory;

import java.util.List;


public class TaskHistoryResponseMapper {

    private TaskHistoryResponseMapper() {
        throw new IllegalArgumentException("Utility class");
    }

    public static FindTaskHistoryResponse toFindTaskHistoryResponse(List<TaskHistory> taskHistories) {
        List<FindTaskHistoryResponse.TaskHistoryResponse> historyResponses = taskHistories.stream()
                .map(taskHistory -> {
                    FindTaskHistoryResponse.Details details =
                            switch (taskHistory.getType()) {
                                case PROCESSOR_CHANGED, PROCESSOR_ASSIGNED -> new FindTaskHistoryResponse.Details(
                                        new FindTaskHistoryResponse.TaskDetails(
                                                taskHistory.getModifiedMember().getNickname()
                                        ),
                                        null,
                                        null
                                );
                                case STATUS_SWITCHED, TASK_TERMINATED -> new FindTaskHistoryResponse.Details(
                                        new FindTaskHistoryResponse.TaskDetails(
                                                taskHistory.getTaskModificationInfo().getModifiedStatus()
                                        ),
                                        null,
                                        null
                                );
                                case COMMENT -> new FindTaskHistoryResponse.Details(
                                        null,
                                        new FindTaskHistoryResponse.CommentDetails(
                                                taskHistory.getComment().getCommentId(),
                                                taskHistory.getModifiedMember().getNickname(),
                                                taskHistory.getModifiedMember().getImageUrl(),
                                                taskHistory.getComment().isModified(),
                                                taskHistory.getComment().getContent()
                                        ),
                                        null
                                );
                                case COMMENT_FILE -> new FindTaskHistoryResponse.Details(
                                        null,
                                        null,
                                        new FindTaskHistoryResponse.CommentFileDetails(
                                                taskHistory.getComment().getCommentId(),
                                                taskHistory.getModifiedMember().getNickname(),
                                                taskHistory.getModifiedMember().getImageUrl(),
                                                taskHistory.getComment().getOriginalName(),
                                                taskHistory.getComment().getFileUrl(),
                                                taskHistory.getComment().getFileSize()
                                        )
                                );
                            };
                    return new FindTaskHistoryResponse.TaskHistoryResponse(
                            taskHistory.getTaskHistoryId(),
                            taskHistory.getCreatedAt().toLocalDate(),
                            taskHistory.getCreatedAt().toLocalTime(),
                            taskHistory.getType(),
                            details
                    );
                })
                .toList();
        return new FindTaskHistoryResponse(historyResponses);
    }
}
