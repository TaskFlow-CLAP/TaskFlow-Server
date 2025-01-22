package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface FindPeriodTaskProcessUsecase {
    Map<String, Long> aggregatePeriodTaskProcess(String period);
}
