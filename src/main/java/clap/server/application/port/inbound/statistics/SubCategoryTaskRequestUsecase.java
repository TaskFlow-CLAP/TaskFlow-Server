package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface SubCategoryTaskRequestUsecase {
    Map<String, Long> subCategoryTaskRequestAggregate(String period, String mainCategory);
}
