package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.task.response.FindTaskHistoryResponse;

import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.TaskHistory;

import java.util.List;


public class TaskHistoryMapper {

    private TaskHistoryMapper() {
        throw new IllegalArgumentException();
    }

    public static FindTaskHistoryResponse toFindTaskHistoryResponse(List<TaskHistory> taskHistories, List<Attachment> attachments) {
        List<FindTaskHistoryResponse.TaskHistoryResponse> historyResponses = taskHistories.stream()
                .map(taskHistory -> {
                    FindTaskHistoryResponse.Details details =
                            switch (taskHistory.getType()) {
                                case PROCESSOR_CHANGED, PROCESSOR_ASSIGNED -> new FindTaskHistoryResponse.Details(
                                        new FindTaskHistoryResponse.ProcessorChanged(
                                                null, //TODO: 이전 처리자 이름 검토
                                                taskHistory.getTaskModificationInfo().getModifiedMember().getNickname()
                                        ),
                                        null,
                                        null,
                                        null
                                );
                                case STATUS_SWITCHED -> new FindTaskHistoryResponse.Details(
                                        null,
                                        new FindTaskHistoryResponse.TaskStatusSwitched(
                                                taskHistory.getTaskModificationInfo().getTask().getTaskStatus().getDescription()
                                        ),
                                        null,
                                        null
                                );
                                case COMMENT -> new FindTaskHistoryResponse.Details(
                                        null,
                                        null,
                                        new FindTaskHistoryResponse.CommentDetails(
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
                                        null,
                                        attachments.stream()
                                                .filter(attachment -> attachment.getComment().getCommentId().equals(taskHistory.getComment().getCommentId()))
                                                .findFirst()
                                                .map(attachment -> new FindTaskHistoryResponse.CommentFileDetails(
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
