package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface FindPeriodTaskRequestUsecase {
    Map<String, Long> aggregatePeriodTaskRequest(String period);
}
