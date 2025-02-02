package clap.server.application.statistics;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;
import clap.server.application.mapper.response.FindTaskStatisticsMapper;
import clap.server.application.port.inbound.statistics.FindSubCategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
public class FindSubCategoryTaskRequestService implements FindSubCategoryTaskRequestUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public List<StatisticsResponse> aggregateSubCategoryTaskRequest(String period, String mainCategory) {
        return FindTaskStatisticsMapper.toStatisticsResponse(taskDocumentPort.findSubCategoryTaskRequestByPeriod(period, mainCategory));
    }
}
