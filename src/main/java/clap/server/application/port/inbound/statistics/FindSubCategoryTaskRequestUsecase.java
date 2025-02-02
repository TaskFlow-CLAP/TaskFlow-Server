package clap.server.application.port.inbound.statistics;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;

import java.util.List;

public interface FindSubCategoryTaskRequestUsecase {
    List<StatisticsResponse> aggregateSubCategoryTaskRequest(String period, String mainCategory);
}
