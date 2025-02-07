package clap.server.application.service.statistics;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;
import clap.server.application.mapper.response.StatisticsResponseMapper;
import clap.server.application.port.inbound.statistics.FindSubCategoryTaskRequestUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindSubCategoryTaskRequestService implements FindSubCategoryTaskRequestUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public List<StatisticsResponse> aggregateSubCategoryTaskRequest(String period, String mainCategory) {
        return StatisticsResponseMapper.toStatisticsResponse(taskDocumentPort.findSubCategoryTaskRequestByPeriod(period, mainCategory));
    }
}
