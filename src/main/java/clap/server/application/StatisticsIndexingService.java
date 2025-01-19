package clap.server.application;

import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
public class StatisticsIndexingService {
    private final LoadTaskPort loadTaskPort;
    private final ElasticTaskPort elasticTaskPort;

    @Scheduled(cron = "0 0 0 * * *")
    public void IndexStatistics() {
        elasticTaskPort.saveStatistics(
                loadTaskPort.findYesterdayTaskByDate(LocalDateTime.now().withNano(0))
                        .stream().map(ElasticTask::new).toList()
        );
    }
}
