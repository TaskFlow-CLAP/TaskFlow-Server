package clap.server.domain.policy.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.common.annotation.architecture.Policy;
import clap.server.domain.model.member.Member;

@Policy
public class ProcessorTaskCountPolicy {

    public void incrementTaskCount(Member processor, TaskStatus status) {
        if (status == TaskStatus.IN_PROGRESS) {
            processor.incrementInProgressTaskCount();
        } else if (status == TaskStatus.IN_REVIEWING) {
            processor.incrementInReviewingTaskCount();
        }
    }

    public void decrementTaskCount(Member processor, TaskStatus status) {
        if (status == TaskStatus.IN_PROGRESS) {
            processor.decrementInProgressTaskCount();
        } else if (status == TaskStatus.IN_REVIEWING) {
            processor.decrementInReviewingTaskCount();
        }
    }
}

