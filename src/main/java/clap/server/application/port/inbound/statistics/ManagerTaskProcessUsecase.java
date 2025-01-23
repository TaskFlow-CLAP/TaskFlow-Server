package clap.server.application.port.inbound.statistics;

import java.util.Map;

public interface ManagerTaskProcessUsecase {
    Map<String, Long> aggregateManagerTaskProcess(String period);
}
