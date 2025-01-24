package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.FindPeriodTaskProcessUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.statistics.Statistics;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindPeriodTaskProcessService implements FindPeriodTaskProcessUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public Map<String, Long> aggregatePeriodTaskProcess(String period) {
        if (period.equals("week") || period.equals("month")) {
            return Statistics.transformToWeekdayStatistics(taskDocumentPort.findPeriodTaskProcessByPeriod(period));
        }
        return taskDocumentPort.findPeriodTaskProcessByPeriod(period);
    }
}
