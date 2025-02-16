package clap.server.application.mapper.response;


import clap.server.adapter.inbound.web.dto.task.response.*;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskResponseMapper {
    private TaskResponseMapper() {
        throw new IllegalArgumentException();
    }

    public static CreateTaskResponse toCreateTaskResponse(Task task) {
        return new CreateTaskResponse(task.getTaskId(), task.getCategory().getCategoryId(), task.getTitle());
    }

    public static FilterRequestedTasksResponse toFilterRequestedTasksResponse(Task task) {
        return new FilterRequestedTasksResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getNickname() : "",
                task.getProcessor() != null ? task.getProcessor().getImageUrl() : "",
                task.getTaskStatus(),
                task.getFinishedAt() != null ? task.getFinishedAt() : null
        );
    }

    public static FilterAssignedTaskListResponse toFilterAssignedTaskListResponse(Task task) {
        return new FilterAssignedTaskListResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getRequester() != null ? task.getRequester().getMemberInfo().getNickname() : "",
                task.getRequester() != null ? task.getRequester().getImageUrl() : "",
                task.getTaskStatus(),
                task.getFinishedAt() != null ? task.getFinishedAt() : null
        );
    }

    public static FilterPendingApprovalResponse toFilterPendingApprovalTasksResponse(Task task) {
        return new FilterPendingApprovalResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getRequester() != null ? task.getRequester().getMemberInfo().getNickname() : "",
                task.getRequester() != null ? task.getRequester().getImageUrl() : ""
        );
    }

    public static FindTaskDetailsResponse toFindTaskDetailResponse(Task task, List<Attachment> attachments) {
        List<AttachmentResponse> attachmentResponses = AttachmentResponseMapper.toAttachmentResponseList(attachments);
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
                approvedTask.getLabel() != null ? approvedTask.getLabel().getLabelName() : "",
                approvedTask.getTaskStatus()
        );
    }

    public static FilterAllTasksResponse toFilterAllTasksResponse(Task task) {
        return new FilterAllTasksResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getCreatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getProcessor() != null ? task.getProcessor().getMemberInfo().getNickname() : "",
                task.getProcessor() != null ? task.getProcessor().getImageUrl() : "",
                task.getRequester() != null ? task.getRequester().getMemberInfo().getNickname() : "",
                task.getRequester() != null ? task.getRequester().getImageUrl() : "",
                task.getTaskStatus(),
                task.getFinishedAt() != null ? task.getFinishedAt() : null
        );
    }

    public static TaskBoardResponse toTaskBoardResponse(List<Task> tasks) {
        Map<TaskStatus, List<TaskItemResponse>> tasksByStatus = tasks.stream()
                .map(TaskResponseMapper::toTaskItemResponse)
                .collect(Collectors.groupingBy(TaskItemResponse::taskStatus));

        return new TaskBoardResponse(
                tasksByStatus.getOrDefault(TaskStatus.IN_PROGRESS, Collections.emptyList()),
                tasksByStatus.getOrDefault(TaskStatus.IN_REVIEWING, Collections.emptyList()),
                tasksByStatus.getOrDefault(TaskStatus.COMPLETED, Collections.emptyList())
        );
    }

    public static TaskItemResponse toTaskItemResponse(Task task) {
        return new TaskItemResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getTitle(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getLabel() != null ? toLabelInfo(task.getLabel()) : null,
                task.getRequester().getNickname(),
                task.getRequester().getImageUrl(),
                task.getRequester().getMemberInfo().getDepartment().getName(),
                task.getProcessorOrder(),
                task.getTaskStatus(),
                task.getCreatedAt()
        );
    }

    public static TaskItemResponse.LabelInfo toLabelInfo(Label label) {
        return new TaskItemResponse.LabelInfo(
                label.getLabelName(),
                label.getLabelColor()
        );
    }

    public static FindTaskDetailsForManagerResponse toFindTaskDetailForManagerResponse(Task task, List<Attachment> attachments) {
        List<AttachmentResponse> attachmentResponses =  AttachmentResponseMapper.toAttachmentResponseList(attachments);
        return new FindTaskDetailsForManagerResponse(
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
                task.getDueDate(),
                task.getLabel() != null ? task.getLabel().getLabelName() : "",
                attachmentResponses
        );
    }

    public static FindApprovalFormResponse toFindApprovalFormResponse(Task task) {
        return new FindApprovalFormResponse(
                task.getCategory().getCategoryId(),
                task.getCategory().getName(),
                task.getCategory().getMainCategory().getName()
        );
    }

    public static FindManagersResponse toFindManagersResponse(Member manager) {
        return new FindManagersResponse(
                manager.getMemberId(),
                manager.getNickname(),
                manager.getImageUrl(),
                manager.getRemainingTasks()
        );
    }

}
