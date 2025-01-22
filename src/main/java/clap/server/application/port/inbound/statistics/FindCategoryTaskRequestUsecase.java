package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface FindCategoryTaskRequestUsecase {
    Map<String, Long> aggregateCategoryTaskRequest(String period);
}
