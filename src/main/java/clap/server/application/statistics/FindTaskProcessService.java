package clap.server.application.statistics;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;
import clap.server.application.mapper.response.FindTaskStatisticsMapper;
import clap.server.application.port.inbound.statistics.FindTaskProcessUsecase;
import clap.server.application.port.outbound.task.TaskDocumentPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.statistics.Statistics;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
class FindTaskProcessService implements FindTaskProcessUsecase {
    private final TaskDocumentPort taskDocumentPort;

    @Override
    public List<StatisticsResponse> aggregateCategoryTaskRequest(String period) {
        return FindTaskStatisticsMapper.toStatisticsResponse(taskDocumentPort.findCategoryTaskRequestByPeriod(period));
    }

    @Override
    public List<StatisticsResponse> aggregateManagerTaskProcess(String period) {
        return FindTaskStatisticsMapper.toStatisticsResponse(taskDocumentPort.findManagerTaskProcessByPeriod(period));
    }

    @Override
    public List<StatisticsResponse> aggregatePeriodTaskProcess(String period) {
        if (period.equals("week") || period.equals("month")) {
            return Statistics.transformToWeekdayStatistics(taskDocumentPort.findPeriodTaskProcessByPeriod(period));
        }
        return FindTaskStatisticsMapper.toStatisticsResponse(taskDocumentPort.findPeriodTaskProcessByPeriod(period));
    }

    @Override
    public List<StatisticsResponse> aggregatePeriodTaskRequest(String period) {
        if (period.equals("week") || period.equals("month")) {
            return Statistics.transformToWeekdayStatistics(taskDocumentPort.findPeriodTaskRequestByPeriod(period));
        }
        return FindTaskStatisticsMapper.toStatisticsResponse(taskDocumentPort.findPeriodTaskRequestByPeriod(period));
    }
}
