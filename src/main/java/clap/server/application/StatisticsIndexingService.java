package clap.server.application;

import clap.server.adapter.outbound.infrastructure.elastic.entity.ElasticTask;
import clap.server.application.port.outbound.task.ElasticTaskPort;
import clap.server.application.port.outbound.task.LoadTaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
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
