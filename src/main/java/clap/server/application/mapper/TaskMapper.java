package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.task.*;

import clap.server.domain.model.task.Attachment;

import clap.server.domain.model.task.Task;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {
    private TaskMapper() {
        throw new IllegalArgumentException();
    }
    public static CreateTaskResponse toCreateTaskResponse(Task task) {
        return new CreateTaskResponse(task.getTaskId(), task.getCategory().getCategoryId(), task.getTitle());
    }

    public static UpdateTaskResponse toUpdateTaskResponse(Task task) {
        return new UpdateTaskResponse(task.getTaskId(), task.getCategory().getCategoryId(), task.getTitle());
    }

    public static FindTaskListResponse toFindTaskListResponse(Task task) {
        return new FindTaskListResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getUpdatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getNickname() : "",
                task.getTaskStatus(),
                task.getCompletedAt() != null ? task.getCompletedAt() : null
        );
    }

    public static List<FindTaskDetailsResponse> toFindTaskDetailResponses(Task task, List<Attachment> attachments){

        List<AttachmentResponse> attachmentResponses = attachments.stream()
                .map(attachment -> new AttachmentResponse(
                        attachment.getAttachmentId(),
                        attachment.getOriginalName(),
                        attachment.getFileSize(),
                        attachment.getFileUrl(),
                        attachment.getCreatedAt()
                ))
                .collect(Collectors.toList());

        FindTaskDetailsResponse response = new FindTaskDetailsResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getCompletedAt(),
                task.getTaskStatus(),
                task.getRequester().getMemberInfo().getNickname(),
                task.getRequester().getImageUrl(),
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getNickname() : "",
                task.getProcessor() != null ? task.getProcessor().getImageUrl() : "",
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getDescription(),
                attachmentResponses
        );

        return List.of(response);
    }
}
