package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.SubCategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class SubCategoryTaskRequestService implements SubCategoryTaskRequestUsecase {
    private final ElasticTaskPort elasticTaskPort;

    @Override
    public Map<String, Long> subCategoryTaskRequestAggregate(String period, String mainCategory) {
        return elasticTaskPort.findSubCategoryTaskRequestByPeriod(period, mainCategory);
    }
}
