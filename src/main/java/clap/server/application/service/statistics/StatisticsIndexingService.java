package clap.server.application.service.statistics;

import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
public class StatisticsIndexingService {
    private final LoadTaskPort loadTaskPort;
    private final TaskDocumentPort taskDocumentPort;

  @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void IndexStatistics() {
        taskDocumentPort.saveStatistics(
                loadTaskPort.findYesterdayTaskByDate(LocalDateTime.now().withNano(0))
        );
    }
}
