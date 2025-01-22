package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.task.AttachmentResponse;
import clap.server.adapter.inbound.web.dto.task.CreateTaskResponse;
import clap.server.adapter.inbound.web.dto.task.FindTaskDetailsResponse;
import clap.server.adapter.inbound.web.dto.task.FindTaskListResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;

import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {
    private TaskMapper() {
        throw new IllegalArgumentException();
    }

    public static Task toTask(Member member, Category category, String title, String description) {
        return Task.builder()
                .title(title)
                .description(description)
                .category(category)
                .requester(member)
                .taskStatus(TaskStatus.REQUESTED)
                .taskCode("1234") //TODO: 하드코딩 제거, reviewer_id 명시 필요
                .build();
    }

    public static CreateTaskResponse toCreateTaskResponse(Task task) {
        return new CreateTaskResponse(task.getTaskId(), task.getCategory().getCategoryId(), task.getTitle());
    }

    public static FindTaskListResponse toFindTaskListResponse(Task task) {
        return new FindTaskListResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getName() : null,
                task.getTaskStatus(),
                task.getCompletedAt()
        );
    }

    public static List<FindTaskDetailsResponse> toFindTaskDetailResponses(Task task, List<Attachment> attachments){

        List<AttachmentResponse> attachmentResponses = attachments.stream()
                .map(attachment -> new AttachmentResponse(
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
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getNickname() : null,
                task.getProcessor() != null ? task.getProcessor().getImageUrl() : null,
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getDescription(),
                attachmentResponses
        );

        return List.of(response);
    }
}
