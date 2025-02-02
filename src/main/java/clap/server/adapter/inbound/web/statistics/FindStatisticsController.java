package clap.server.adapter.inbound.web.statistics;

import clap.server.adapter.inbound.web.dto.statistics.PeriodType;
import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;
import clap.server.adapter.inbound.web.dto.statistics.StatisticsType;
import clap.server.application.port.inbound.statistics.FindSubCategoryTaskRequestUsecase;
import clap.server.application.port.inbound.statistics.FindTaskProcessUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import clap.server.exception.StatisticsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static clap.server.exception.code.StatisticsErrorCode.STATISTICS_BAD_REQUEST;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "02. Task [통계]", description = "작업 통계 API")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/tasks/statistics")
public class FindStatisticsController {
    private final FindTaskProcessUsecase findTaskProcessUsecase;
    private final FindSubCategoryTaskRequestUsecase findSubCategoryTaskRequestUsecase;


    @Operation(summary = "기본 통계 API")
    @Parameter(name = "periodType", required = true, in = QUERY)
    @Parameter(name = "statisticsType", required = true, in = QUERY)
    @GetMapping
    @Secured("ROLE_MANAGER")
    public ResponseEntity<List<StatisticsResponse>> aggregateTaskStatistics(@RequestParam PeriodType periodType, @RequestParam StatisticsType statisticsType) {
        System.out.println("periodType = " + periodType);
        System.out.println("statisticsType = " + statisticsType);
        return switch (statisticsType) {
            case REQUEST_BY_PERIOD ->
                    ResponseEntity.ok(findTaskProcessUsecase.aggregatePeriodTaskRequest(periodType.getType()));
            case PROCESS_BY_PERIOD -> ResponseEntity.ok(findTaskProcessUsecase
                    .aggregatePeriodTaskProcess(periodType.getType()));
            case REQUEST_BY_CATEGORY ->
                    ResponseEntity.ok(findTaskProcessUsecase.aggregateCategoryTaskRequest(periodType.getType()));
            case PROCESS_BY_MANAGER -> ResponseEntity.ok(findTaskProcessUsecase
                    .aggregateManagerTaskProcess(periodType.getType()));
            default -> throw new StatisticsException(STATISTICS_BAD_REQUEST);
        };
    }

    @Operation(summary = "1차 카테고리 하위 2차 카테고리별 통계 API")
    @Parameter(name = "periodType", description = "day, week, month", required = true, in = QUERY)
    @Parameter(name = "mainCategory", description = "1차 카테고리 이름", required = true, in = QUERY)
    @GetMapping("/subcategory")
    @Secured("ROLE_MANAGER")
    public ResponseEntity<List<StatisticsResponse>> aggregateSubCategoryTaskRequest(@RequestParam PeriodType periodType, @RequestParam String mainCategory) {
        return ResponseEntity.ok(findSubCategoryTaskRequestUsecase
                .aggregateSubCategoryTaskRequest(periodType.getType(), mainCategory));
    }
}