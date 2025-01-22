package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.FindCategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindCategoryTaskRequestService implements FindCategoryTaskRequestUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public Map<String, Long> aggregateCategoryTaskRequest(String period) {
        return taskDocumentPort.findCategoryTaskRequestByPeriod(period);
    }
}
