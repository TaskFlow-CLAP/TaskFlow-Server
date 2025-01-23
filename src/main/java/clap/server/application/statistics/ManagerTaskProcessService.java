package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.ManagerTaskProcessUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class ManagerTaskProcessService implements ManagerTaskProcessUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public Map<String, Long> aggregateManagerTaskProcess(String period) {
        return taskDocumentPort.findManagerTaskProcessByPeriod(period);
    }
}
