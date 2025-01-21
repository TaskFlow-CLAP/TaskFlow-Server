package clap.server.adapter.inbound.web.statistics;

import clap.server.application.port.inbound.statistics.CategoryTaskRequestUsecase;
import clap.server.application.port.inbound.statistics.PeriodTaskProcessUsecase;
import clap.server.application.port.inbound.statistics.PeriodTaskRequestUsecase;
import clap.server.common.annotation.architecture.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@WebAdapter
@RequiredArgsConstructor
public class StatisticsFindController {
    private final PeriodTaskRequestUsecase periodTaskRequestUsecase;
    private final PeriodTaskProcessUsecase periodTaskProcessUsecase;
    private final CategoryTaskRequestUsecase categoryTaskRequestUsecase;
//    private final SubCategoryTaskRequestUsecase subCategoryTaskRequestUsecase;
//    private final ManagerTaskProcessUsecase managerTaskProcessUsecase;

    @GetMapping(value = "/task/statistics/task-requests-by-period")
    public Map<String, Long> aggregatePeriodTaskRequest(@RequestParam String period) {
        return periodTaskRequestUsecase.periodTaskRequestAggregate(period);
    }

    @GetMapping("/task/statistics/task-processed-by-period")
    public Map<String, Long> aggregatePeriodTaskProcess(@RequestParam String period) {
        return periodTaskProcessUsecase.periodTaskProcessAggregate(period);
    }
    @GetMapping("/task/statistics/task-requests-by-category")
    public Map<String, Long> aggregateCategoryTaskRequest(@RequestParam String period) {
        return categoryTaskRequestUsecase.categoryTaskRequestAggregate(period);
    }

//    @GetMapping("/task/statistics/task-requests-by-subcategory")
//
//    @GetMapping("/task/statistics/tasks-processed-by-manager")
}