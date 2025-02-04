package clap.server.domain.policy.task;

import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.task.Task;
import clap.server.exception.DomainException;
import clap.server.exception.code.TaskErrorCode;

import static clap.server.domain.policy.task.TaskPolicyConstants.DEFAULT_PROCESSOR_ORDER_GAP;

@Policy
public class TaskOrderCalculationPolicy {

    public long calculateOrderForTop(Task prevTask, Task nextTask) {
        Long prevTaskOrder = prevTask == null ? null : prevTask.getProcessorOrder();
        if (prevTaskOrder == null) {
            return nextTask.getProcessorOrder() - DEFAULT_PROCESSOR_ORDER_GAP;
        } else return calculateNewProcessorOrder(prevTaskOrder, nextTask.getProcessorOrder());
    }

    public long calculateOrderForBottom(Task prevTask, Task nextTask) {
        Long nextTaskOrder = nextTask == null ? null : nextTask.getProcessorOrder();
        if (nextTaskOrder == null) {
            return prevTask.getProcessorOrder() + DEFAULT_PROCESSOR_ORDER_GAP;
        } else return calculateNewProcessorOrder(prevTask.getProcessorOrder(), nextTaskOrder);
    }

    public long calculateNewProcessorOrder(Long prevTaskOrder, Long nextTaskOrder) {
        if (prevTaskOrder != null && nextTaskOrder != null) {
            if (nextTaskOrder - prevTaskOrder < 2) {
                throw new DomainException(TaskErrorCode.INVALID_TASK_ORDER);
            }
            return (prevTaskOrder + nextTaskOrder) / 2;
        }
        // 기본값 (예외적인 상황 방지)
        else {
            return DEFAULT_PROCESSOR_ORDER_GAP;
        }
    }

}

