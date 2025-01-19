package clap.server.application;

import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class StatisticsService {
    private final ElasticTaskPort elasticTaskPort;
}
