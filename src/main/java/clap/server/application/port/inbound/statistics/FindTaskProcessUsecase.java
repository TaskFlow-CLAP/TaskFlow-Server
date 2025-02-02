package clap.server.application.port.inbound.statistics;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;

import java.util.List;

public interface FindTaskProcessUsecase {
    List<StatisticsResponse> aggregateCategoryTaskRequest(String period);
    List<StatisticsResponse> aggregateManagerTaskProcess(String period);
    List<StatisticsResponse> aggregatePeriodTaskProcess(String period);
    List<StatisticsResponse> aggregatePeriodTaskRequest(String period);
}
