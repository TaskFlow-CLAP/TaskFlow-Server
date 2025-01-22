package clap.server.application.statistics;

import clap.server.application.port.inbound.statistics.FindSubCategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindSubCategoryTaskRequestService implements FindSubCategoryTaskRequestUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public Map<String, Long> aggregateSubCategoryTaskRequest(String period, String mainCategory) {
        return taskDocumentPort.findSubCategoryTaskRequestByPeriod(period, mainCategory);
    }
}
