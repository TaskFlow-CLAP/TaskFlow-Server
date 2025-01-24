package clap.server.adapter.inbound.web.statistics;

import clap.server.adapter.inbound.web.dto.statistics.PeriodType;
import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;
import clap.server.adapter.inbound.web.dto.statistics.StatisticsType;
import clap.server.application.port.inbound.statistics.*;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.exception.StatisticsException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static clap.server.exception.code.StatisticsErrorCode.STATISTICS_BAD_REQUEST;

@Tag(name = "작업 관련 통계")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/tasks/statistics")
public class FindStatisticsController {
    private final FindPeriodTaskRequestUsecase findPeriodTaskRequestUsecase;
    private final FindPeriodTaskProcessUsecase findPeriodTaskProcessUsecase;
    private final FindCategoryTaskRequestUsecase findCategoryTaskRequestUsecase;
    private final FindSubCategoryTaskRequestUsecase findSubCategoryTaskRequestUsecase;
    private final FindManagerTaskProcessUsecase findManagerTaskProcessUsecase;

    @GetMapping
    public ResponseEntity<List<StatisticsResponse>> aggregateTaskStatistics(@RequestParam PeriodType periodType, @RequestParam StatisticsType statisticsType) {
        switch (statisticsType) {
            case REQUEST_BY_PERIOD ->
                    ResponseEntity.ok(findPeriodTaskRequestUsecase
                    .aggregatePeriodTaskRequest(periodType.getType())
                    .entrySet()
                    .stream()
                    .map(result -> new StatisticsResponse(result.getKey(), result.getValue()))
                    .toList());
            case PROCESS_BY_PERIOD ->
                    ResponseEntity.ok(findPeriodTaskProcessUsecase
                    .aggregatePeriodTaskProcess(periodType.getType())
                    .entrySet()
                    .stream()
                    .map(result -> new StatisticsResponse(result.getKey(), result.getValue()))
                    .toList());
            case REQUEST_BY_CATEGORY ->
                    ResponseEntity.ok(findCategoryTaskRequestUsecase.aggregateCategoryTaskRequest(periodType.getType())
                            .entrySet()
                            .stream()
                            .map(result -> new StatisticsResponse(result.getKey(), result.getValue()))
                            .toList());
            case PROCESS_BY_MANAGER ->
                    ResponseEntity.ok(findManagerTaskProcessUsecase
                    .aggregateManagerTaskProcess(periodType.getType())
                    .entrySet()
                    .stream()
                    .map(result -> new StatisticsResponse(result.getKey(), result.getValue()))
                    .toList());
        }
        throw new StatisticsException(STATISTICS_BAD_REQUEST);
    }

    @GetMapping("/subcategory")
    public ResponseEntity<List<StatisticsResponse>> aggregateSubCategoryTaskRequest(@RequestParam String period, @RequestParam String mainCategory) {
        return ResponseEntity.ok(findSubCategoryTaskRequestUsecase
                .aggregateSubCategoryTaskRequest(period, mainCategory)
                .entrySet()
                .stream()
                .map(result -> new StatisticsResponse(result.getKey(), result.getValue()))
                .toList());
    }
}