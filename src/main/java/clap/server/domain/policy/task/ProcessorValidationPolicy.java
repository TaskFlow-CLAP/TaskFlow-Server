package clap.server.domain.policy.task;

import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.task.Task;
import clap.server.exception.ApplicationException;
import clap.server.exception.code.TaskErrorCode;

import java.util.Objects;

@Policy
public class ProcessorValidationPolicy {
    public void validateProcessor(final Long processorId,final Task targetTask) {
        if (!Objects.equals(processorId, targetTask.getProcessor().getMemberId())) {
            throw new ApplicationException(TaskErrorCode.NOT_A_PROCESSOR);
        }
    }
}
