package clap.server.domain.policy.task;

import clap.server.common.annotation.architecture.Policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

@Policy
public class TaskStatisticsPolicy {

    public Map<String, Long> transformToWeekdayStatistics(Map<String, Long> statistics) {
        TreeMap<String, Long> result = new TreeMap<>();

        for (Entry<String, Long> statistic : statistics.entrySet()) {
            String stringDate = statistic.getKey();
            LocalDate date = LocalDate.parse(stringDate);

            if (!(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                result.put(stringDate.substring(6, 10).replace("-", "월 ") + "일", statistic.getValue());
            }
        }
        return result;
    }
}
