package clap.server.domain.model.task.policy;

import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;

import java.util.Objects;

@Policy
public class TaskOrderRequestPolicy {

    public void validateProcessor(Long processorId, Task targetTask) {
        if (!Objects.equals(processorId, targetTask.getProcessor().getMemberId())) {
            throw new ApplicationException(TaskErrorCode.NOT_A_PROCESSOR);
        }
    }
}
