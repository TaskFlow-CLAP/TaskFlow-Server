package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface PeriodTaskProcessUsecase {
    Map<String, Long> periodTaskProcessAggregate(String period);
}
