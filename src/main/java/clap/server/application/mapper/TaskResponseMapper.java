package clap.server.application.mapper;


import clap.server.adapter.inbound.web.dto.task.response.*;
import clap.server.adapter.outbound.persistense.entity.task.LabelEntity;
import clap.server.adapter.outbound.persistense.entity.task.TaskEntity;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Attachment;
import clap.server.domain.model.task.Label;
import clap.server.domain.model.task.Task;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static clap.server.application.mapper.AttachmentMapper.toAttachmentResponseList;

public class TaskResponseMapper {
    private TaskResponseMapper() {
        throw new IllegalArgumentException();
    }

    public static CreateTaskResponse toCreateTaskResponse(Task task) {
        return new CreateTaskResponse(task.getTaskId(), task.getCategory().getCategoryId(), task.getTitle());
    }

    public static UpdateTaskResponse toUpdateTaskResponse(Task task) {
        return new UpdateTaskResponse(task.getTaskId(), task.getCategory().getCategoryId(), task.getTitle());
    }

    public static FilterRequestedTasksResponse toFilterRequestedTasksResponse(Task task) {
        return new FilterRequestedTasksResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getUpdatedAt(),
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
                task.getUpdatedAt(),
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
                task.getUpdatedAt(),
                task.getCategory().getMainCategory().getName(),
                task.getCategory().getName(),
                task.getTitle(),
                task.getRequester() != null ? task.getRequester().getMemberInfo().getNickname() : "",
                task.getRequester() != null ? task.getRequester().getImageUrl() : ""
        );
    }

    public static FindTaskDetailsResponse toFindTaskDetailResponse(Task task, List<Attachment> attachments) {
        List<AttachmentResponse> attachmentResponses = toAttachmentResponseList(attachments);
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

    public static FilterAllTasksResponse toFilterAllTasksResponse(Task task) {
        return new FilterAllTasksResponse(
                task.getTaskId(),
                task.getTaskCode(),
                task.getUpdatedAt(),
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
                tasksByStatus.getOrDefault(TaskStatus.PENDING_COMPLETED, Collections.emptyList()),
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
        List<AttachmentResponse> attachmentResponses = toAttachmentResponseList(attachments);
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

    public static FindManagersResponse toFindManagersResponse(Member manager, int remainingTasks) {
        return new FindManagersResponse(
                manager.getMemberId(),
                manager.getNickname(),
                manager.getImageUrl(),
                remainingTasks
        );
    }

    public static TeamStatusResponse toTeamStatusResponse(List<TaskEntity> taskEntities) {
        // 담당자별로 그룹화
        Map<Long, List<TaskEntity>> tasksByProcessor = taskEntities.stream()
                .collect(Collectors.groupingBy(taskEntity -> taskEntity.getProcessor().getMemberId()));

        List<TeamTaskResponse> memberResponses = tasksByProcessor.entrySet().stream()
                .map(entry -> {
                    List<TeamTaskItemResponse> teamtaskItemResponses = entry.getValue().stream()
                            .map(TaskResponseMapper::toTeamTaskItemResponse)
                            .collect(Collectors.toList());

                    return new TeamTaskResponse(
                            entry.getKey(),
                            entry.getValue().get(0).getProcessor().getNickname(),
                            entry.getValue().get(0).getProcessor().getImageUrl(),
                            entry.getValue().get(0).getProcessor().getDepartment().getName(),
                            (int) entry.getValue().stream().filter(t -> t.getTaskStatus() == TaskStatus.IN_PROGRESS).count(),
                            (int) entry.getValue().stream().filter(t -> t.getTaskStatus() == TaskStatus.PENDING_COMPLETED).count(),
                            entry.getValue().size(),
                            teamtaskItemResponses
                    );
                })
                .collect(Collectors.toList());

        return new TeamStatusResponse(memberResponses);
    }

    public static TeamTaskItemResponse toTeamTaskItemResponse(TaskEntity taskEntity) {
        return new TeamTaskItemResponse(
                taskEntity.getTaskId(),
                taskEntity.getTaskCode(),
                taskEntity.getTitle(),
                taskEntity.getCategory().getMainCategory().getName(),
                taskEntity.getCategory().getName(),
                taskEntity.getLabel() != null ? toLabelInfo(taskEntity.getLabel()) : null,
                taskEntity.getRequester().getNickname(),
                taskEntity.getRequester().getImageUrl(),
                taskEntity.getRequester().getDepartment().getName(),
                taskEntity.getProcessorOrder(),
                taskEntity.getTaskStatus(),
                taskEntity.getCreatedAt()
        );
    }

    public static TeamTaskItemResponse.LabelInfo toLabelInfo(LabelEntity label) { // Label → LabelEntity로 변경
        return new TeamTaskItemResponse.LabelInfo(
                label.getLabelName(),
                label.getLabelColor()
        );
    }






}
