package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.PeriodTaskRequestUsecase;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class PeriodTaskRequestService implements PeriodTaskRequestUsecase {
    private final ElasticTaskPort elasticTaskPort;

    @Override
    public Map<String, Long> periodTaskAggregate(String period) {
        return elasticTaskPort.findPeriodTaskRequestByPeriod(period);
    }
}
