package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface FindSubCategoryTaskRequestUsecase {
    Map<String, Long> aggregateSubCategoryTaskRequest(String period, String mainCategory);
}
