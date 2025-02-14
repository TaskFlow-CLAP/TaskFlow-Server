package clap.server.application.service.task;

import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.application.port.outbound.member.CommandMemberPort;
import clap.server.domain.model.member.Member;
import clap.server.domain.policy.task.ProcessorTaskCountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProcessorTaskCountService {

    private final ProcessorTaskCountPolicy processorTaskCountPolicy;
    private final CommandMemberPort commandMemberPort;

    public void handleTaskStatusChange(Member processor, TaskStatus oldStatus, TaskStatus newStatus) {
        processorTaskCountPolicy.decrementTaskCount(processor, oldStatus);
        processorTaskCountPolicy.incrementTaskCount(processor, newStatus);
        commandMemberPort.save(processor);
    }

    public void handleProcessorChange(Member oldProcessor, Member newProcessor, TaskStatus status) {
        processorTaskCountPolicy.decrementTaskCount(oldProcessor, status);
        processorTaskCountPolicy.incrementTaskCount(newProcessor, status);
        commandMemberPort.save(oldProcessor);
        commandMemberPort.save(newProcessor);
    }
}
