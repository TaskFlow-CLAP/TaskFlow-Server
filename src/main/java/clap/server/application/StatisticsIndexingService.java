package clap.server.application;

import clap.server.adapter.outbound.infrastructure.elastic.document.ElasticTask;
import clap.server.application.port.outbound.ElasticTaskPort;
import clap.server.application.port.outbound.TaskPort;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticsIndexingService {
    private final TaskPort taskPort;
    private final ElasticTaskPort elasticTaskPort;

    @Scheduled(cron = "0 0 0 * * *")
    public void IndexStatistics() {
        elasticTaskPort.saveStatistics(
                taskPort.findYesterdayTaskByDate(LocalDateTime.now().withNano(0))
                        .stream().map(ElasticTask::new).toList()
        );
    }
}
