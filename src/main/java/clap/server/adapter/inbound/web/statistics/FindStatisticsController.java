package clap.server.adapter.inbound.web.statistics;

import clap.server.application.port.inbound.statistics.*;
import clap.server.common.annotation.architecture.WebAdapter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Tag(name = "작업 관련 통계")
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/api/tasks/statistics")
public class FindStatisticsController {
    private final FindPeriodTaskRequestUsecase findPeriodTaskRequestUsecase;
    private final FindPeriodTaskProcessUsecase findPeriodTaskProcessUsecase;
    private final FindCategoryTaskRequestUsecase findCategoryTaskRequestUsecase;
    private final FindSubCategoryTaskRequestUsecase findSubCategoryTaskRequestUsecase;
    private final ManagerTaskProcessUsecase managerTaskProcessUsecase;

    @GetMapping(value = "/task-requests-by-period")
    public ResponseEntity<Map<String, Long>> aggregatePeriodTaskRequest(@RequestParam String period) {
        return ResponseEntity.ok(findPeriodTaskRequestUsecase.aggregatePeriodTaskRequest(period));
    }

    @GetMapping("/task-processed-by-period")
    public ResponseEntity<Map<String, Long>> aggregatePeriodTaskProcess(@RequestParam String period) {
        return ResponseEntity.ok(findPeriodTaskProcessUsecase.aggregatePeriodTaskProcess(period));
    }

    @GetMapping("/task-requests-by-category")
    public ResponseEntity<Map<String, Long>> aggregateCategoryTaskRequest(@RequestParam String period) {
        return ResponseEntity.ok(findCategoryTaskRequestUsecase.aggregateCategoryTaskRequest(period));
    }

    @GetMapping("/task-requests-by-subcategory")
    public ResponseEntity<Map<String, Long>> aggregateSubCategoryTaskRequest(@RequestParam String period, @RequestParam String mainCategory) {
        return ResponseEntity.ok(findSubCategoryTaskRequestUsecase.aggregateSubCategoryTaskRequest(period, mainCategory));
    }

    @GetMapping("/tasks-processed-by-manager")
    public ResponseEntity<Map<String, Long>> aggregateSubCategoryTaskRequest(@RequestParam String period) {
        return ResponseEntity.ok(managerTaskProcessUsecase.aggregateManagerTaskProcess(period));
    }
}