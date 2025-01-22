package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.CategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class CategoryTaskRequestService implements CategoryTaskRequestUsecase {
    private final ElasticTaskPort elasticTaskPort;

    @Override
    public Map<String, Long> categoryTaskRequestAggregate(String period) {
        return elasticTaskPort.findCategoryTaskRequestByPeriod(period);
    }
}
