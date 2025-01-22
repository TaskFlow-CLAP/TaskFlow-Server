package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.FindPeriodTaskRequestUsecase;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindPeriodTaskRequestService implements FindPeriodTaskRequestUsecase {
    private final ElasticTaskPort elasticTaskPort;

    @Override
    public Map<String, Long> aggregatePeriodTaskRequest(String period) {
        return elasticTaskPort.findPeriodTaskRequestByPeriod(period);
    }
}
