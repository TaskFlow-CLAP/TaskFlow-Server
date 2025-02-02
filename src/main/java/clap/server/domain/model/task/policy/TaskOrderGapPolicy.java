package clap.server.domain.model.task.policy;

import lombok.Getter;

@Getter
public class TaskOrderGapPolicy {
    public static final long DEFAULT_PROCESSOR_ORDER_GAP = (long) Math.pow(2,6);
}