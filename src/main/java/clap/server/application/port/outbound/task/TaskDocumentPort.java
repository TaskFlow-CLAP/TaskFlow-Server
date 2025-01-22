package clap.server.application.port.outbound.task;

import clap.server.domain.model.task.Task;

import java.util.List;
import java.util.Map;

public interface TaskDocumentPort {
    void saveStatistics(List<Task> statistics);

    Map<String, Long> findPeriodTaskRequestByPeriod(String period);

    Map<String, Long> findPeriodTaskProcessByPeriod(String period);

    Map<String, Long> findCategoryTaskRequestByPeriod(String period);

    Map<String, Long> findSubCategoryTaskRequestByPeriod(String period, String mainCategory);
}
