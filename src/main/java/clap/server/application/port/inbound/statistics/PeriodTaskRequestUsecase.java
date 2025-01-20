package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface PeriodTaskRequestUsecase {
    Map<String, Long> periodTaskRequestAggregate(String period);
}
