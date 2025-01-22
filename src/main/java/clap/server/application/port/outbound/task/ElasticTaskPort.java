package clap.server.application.port.outbound.task;

import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;

import java.util.List;
import java.util.Map;

public interface ElasticTaskPort {
    void saveStatistics(List<ElasticTask> statistics);

    Map<String, Long> findPeriodTaskRequestByPeriod(String period);

    Map<String, Long> findPeriodTaskProcessByPeriod(String period);

    Map<String, Long> findCategoryTaskRequestByPeriod(String period);

    Map<String, Long> findSubCategoryTaskRequestByPeriod(String period, String mainCategory);
}
