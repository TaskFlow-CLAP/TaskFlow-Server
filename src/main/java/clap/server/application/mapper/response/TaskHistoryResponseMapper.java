package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.history.response.FindTaskHistoryResponse;

import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.TaskHistory;

import java.util.List;


public class TaskHistoryResponseMapper {

    private TaskHistoryResponseMapper() {
        throw new IllegalArgumentException("Utility class");
    }

    public static FindTaskHistoryResponse toFindTaskHistoryResponse(List<TaskHistory> taskHistories, List<Attachment> attachments) {
        List<FindTaskHistoryResponse.TaskHistoryResponse> historyResponses = taskHistories.stream()
                .map(taskHistory -> {
                    FindTaskHistoryResponse.Details details =
                            switch (taskHistory.getType()) {
                                case PROCESSOR_CHANGED, PROCESSOR_ASSIGNED -> new FindTaskHistoryResponse.Details(
                                        new FindTaskHistoryResponse.TaskDetails(
                                                taskHistory.getTaskModificationInfo().getModifiedMember().getNickname()
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
                                                taskHistory.getComment().getMember().getNickname(),
                                                taskHistory.getComment().getMember().getImageUrl(),
                                                taskHistory.getComment().isModified(),
                                                taskHistory.getComment().getContent()
                                        ),
                                        null
                                );
                                case COMMENT_FILE -> new FindTaskHistoryResponse.Details(
                                        null,
                                        null,
                                        attachments.stream()
                                                .filter(attachment -> attachment.getComment().getCommentId().equals(taskHistory.getComment().getCommentId()))
                                                .findFirst()
                                                .map(attachment -> new FindTaskHistoryResponse.CommentFileDetails(
                                                        taskHistory.getComment().getCommentId(),
                                                        taskHistory.getComment().getMember().getNickname(),
                                                        taskHistory.getComment().getMember().getImageUrl(),
                                                        taskHistory.getComment().isModified(),
                                                        attachment.getOriginalName(),
                                                        attachment.getFileUrl(),
                                                        attachment.getFileSize()
                                                ))
                                                .orElse(null)
                                );
                            };
                    return new FindTaskHistoryResponse.TaskHistoryResponse(
                            taskHistory.getTaskHistoryId(),
                            taskHistory.getUpdatedAt().toLocalDate(),
                            taskHistory.getUpdatedAt().toLocalTime(),
                            taskHistory.getType(),
                            details
                    );
                })
                .toList();
        return new FindTaskHistoryResponse(historyResponses);
    }
}
