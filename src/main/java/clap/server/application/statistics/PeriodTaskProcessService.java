package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.PeriodTaskProcessUsecase;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class PeriodTaskProcessService implements PeriodTaskProcessUsecase {
    private final ElasticTaskPort elasticTaskPort;

    @Override
    public Map<String, Long> periodTaskProcessAggregate(String period) {
        return elasticTaskPort.findPeriodTaskProcessByPeriod(period);
    }
}
