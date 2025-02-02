package clap.server.domain.policy.task;

import lombok.Getter;

@Getter
public class TaskOrderGapPolicy {
    public static final long DEFAULT_PROCESSOR_ORDER_GAP = (long) Math.pow(2,6);
}