package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface FindManagerTaskProcessUsecase {
    Map<String, Long> aggregateManagerTaskProcess(String period);
}
