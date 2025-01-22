package clap.server.application;

import clap.server.application.port.outbound.task.LoadTaskPort;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
public class StatisticsIndexingService {
    private final LoadTaskPort loadTaskPort;
    private final TaskDocumentPort taskDocumentPort;

    @Scheduled(cron = "0 0 0 * * *")
    public void IndexStatistics() {
        taskDocumentPort.saveStatistics(
                loadTaskPort.findYesterdayTaskByDate(LocalDateTime.now().withNano(0))
        );
    }
}
