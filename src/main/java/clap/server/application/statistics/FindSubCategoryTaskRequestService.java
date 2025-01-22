package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.FindSubCategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindSubCategoryTaskRequestService implements FindSubCategoryTaskRequestUsecase {
    private final ElasticTaskPort elasticTaskPort;

    @Override
    public Map<String, Long> aggregateSubCategoryTaskRequest(String period, String mainCategory) {
        return elasticTaskPort.findSubCategoryTaskRequestByPeriod(period, mainCategory);
    }
}
