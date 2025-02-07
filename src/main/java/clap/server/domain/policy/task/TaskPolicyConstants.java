package clap.server.domain.policy.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class TaskPolicyConstants {
    public static final long DEFAULT_PROCESSOR_ORDER_GAP = (long) Math.pow(2, 6);

    public static final List<TaskStatus> TASK_BOARD_STATUS_FILTER = List.of(
            TaskStatus.IN_PROGRESS,
            TaskStatus.IN_REVIEWING,
            TaskStatus.COMPLETED);

    public static final List<TaskStatus> TASK_UPDATABLE_STATUS = List.of(
            TaskStatus.REQUESTED,
            TaskStatus.IN_PROGRESS,
            TaskStatus.IN_REVIEWING,
            TaskStatus.COMPLETED
    );

    public static final int TASK_MAX_FILE_COUNT = 5;
}