package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.FindPeriodTaskRequestUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.statistics.Statistics;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindPeriodTaskRequestService implements FindPeriodTaskRequestUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public Map<String, Long> aggregatePeriodTaskRequest(String period) {
        if (period.equals("week") || period.equals("month")) {
            return Statistics.transformToWeekdayStatistics(taskDocumentPort.findPeriodTaskRequestByPeriod(period));
        }
        return taskDocumentPort.findPeriodTaskRequestByPeriod(period);
    }
}
