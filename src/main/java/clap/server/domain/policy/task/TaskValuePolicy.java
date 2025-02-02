package clap.server.domain.policy.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import lombok.Getter;

@Getter
public class TaskValuePolicy {
    public static final long DEFAULT_PROCESSOR_ORDER_GAP = (long) Math.pow(2,6);

    public static final TaskStatus[] TASK_BOARD_STATUS_FILTER = {
            TaskStatus.IN_PROGRESS,
            TaskStatus.PENDING_COMPLETED,
            TaskStatus.COMPLETED};
}