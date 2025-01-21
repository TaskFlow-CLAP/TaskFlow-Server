package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface CategoryTaskRequestUsecase {
    Map<String, Long> categoryTaskRequestAggregate(String period);
}
