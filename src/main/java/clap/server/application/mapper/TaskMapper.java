package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.task.*;
import clap.server.adapter.inbound.web.dto.task.response.TaskBoardResponse;
import clap.server.adapter.inbound.web.dto.task.response.TaskItemResponse;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Task;
import org.springframework.data.domain.Slice;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    public static FilterTaskListResponse toFilterTaskListResponse(Task task) {
        return new FilterTaskListResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getUpdatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getNickname() : "",
                task.getTaskStatus(),
                task.getFinishedAt() != null ? task.getFinishedAt() : null
        );
    }

    public static FilterPendingApprovalResponse toFilterPendingApprovalTasksResponse(Task task) {
        return new FilterPendingApprovalResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getUpdatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getRequester().getMemberInfo().getNickname()
        );
    }

    public static FindTaskDetailsResponse toFindTaskDetailResponse(Task task, List<Attachment> attachments){

        List<AttachmentResponse> attachmentResponses = attachments.stream()
                .map(attachment -> new AttachmentResponse(
                        attachment.getAttachmentId(),
                        attachment.getOriginalName(),
                        attachment.getFileSize(),
                        attachment.getFileUrl(),
                        attachment.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new FindTaskDetailsResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getFinishedAt(),
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
    }

    public static ApprovalTaskResponse toApprovalTaskResponse(Task approvedTask) {
        return new ApprovalTaskResponse(
                approvedTask.getTaskId(),
                approvedTask.getProcessor().getNickname(),
                approvedTask.getReviewer().getNickname(),
                approvedTask.getDueDate(),
                approvedTask.getLabel().getLabelName(),
                approvedTask.getTaskStatus()
        );
    }

    public static TaskBoardResponse toSliceTaskItemResponse(Slice<Task> tasks) {
        Map<TaskStatus, List<TaskItemResponse>> tasksByStatus =tasks.getContent().stream()
                .map(TaskMapper::toTaskItemResponse)
                .collect(Collectors.groupingBy(TaskItemResponse::taskStatus));

        return new TaskBoardResponse(
                tasksByStatus.getOrDefault(TaskStatus.IN_PROGRESS, Collections.emptyList()),
                tasksByStatus.getOrDefault(TaskStatus.PENDING_COMPLETED, Collections.emptyList()),
                tasksByStatus.getOrDefault(TaskStatus.COMPLETED, Collections.emptyList()),
                tasks.hasNext(),
                tasks.isFirst(),
                tasks.isLast()
        );
    }

    private static TaskItemResponse toTaskItemResponse(Task task) {
        return new TaskItemResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getRequester().getNickname(),
                task.getRequester().getImageUrl(),
                task.getRequester().getMemberInfo().getDepartment().getName(),
                task.getProcessorOrder(),
                task.getTaskStatus(),
                task.getCreatedAt()
        );
    }
}
