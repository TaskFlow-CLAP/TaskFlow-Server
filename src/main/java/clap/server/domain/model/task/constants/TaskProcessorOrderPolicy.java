package clap.server.domain.model.task.constants;

import lombok.Getter;

@Getter
public class TaskProcessorOrderPolicy {
    public static final long DEFAULT_PROCESSOR_ORDER_GAP = (long) Math.pow(2,6);
}