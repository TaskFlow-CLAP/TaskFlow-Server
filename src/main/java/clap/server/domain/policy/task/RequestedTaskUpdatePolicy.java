package clap.server.domain.policy.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.task.Task;
import clap.server.exception.DomainException;
import clap.server.exception.code.TaskErrorCode;

@Policy
public class RequestedTaskUpdatePolicy {
    public void validateTaskRequested(Task task) {
        if (task.getTaskStatus() != TaskStatus.REQUESTED) {
            throw new DomainException(TaskErrorCode.TASK_STATUS_MISMATCH);
        }
    }
}
