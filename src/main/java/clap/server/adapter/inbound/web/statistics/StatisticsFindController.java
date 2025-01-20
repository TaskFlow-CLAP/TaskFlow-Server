package clap.server.adapter.inbound.web.statistics;

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
//    private final PeriodTaskProcessUsecase periodTaskProcessUsecase;
//    private final CategoryTaskRequestUsecase categoryTaskRequestUsecase;
//    private final SubCategoryTaskRequestUsecase subCategoryTaskRequestUsecase;
//    private final ManagerTaskProcessUsecase managerTaskProcessUsecase;

    @GetMapping(value = "/task/statistics/task-requests-by-period")
    public Map<String, Long> aggregatePeriodTaskRequest(@RequestParam String period) {
        return periodTaskRequestUsecase.periodTaskRequestAggregate(period);
    }

//    @GetMapping("/task/statistics/task-processed-by-period")
//
//    @GetMapping("/task/statistics/task-requests-by-category")
//
//    @GetMapping("/task/statistics/task-requests-by-subcategory")
//
//    @GetMapping("/task/statistics/tasks-processed-by-manager")
}