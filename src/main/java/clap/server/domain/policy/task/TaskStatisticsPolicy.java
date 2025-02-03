package clap.server.domain.policy.task;

import clap.server.common.annotation.architecture.Policy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Policy
public class TaskStatisticsPolicy {
    private static final String DISPLAY_FORMAT = "MM월 dd일";

    public Map<String, Long> filterAndFormatWeekdayStatistics(Map<String, Long> statistics) {
        return statistics.entrySet().stream()
                .filter(this::isWeekday)
                .collect(Collectors.toMap(
                        entry -> formatDate(entry.getKey()),
                        Entry::getValue,
                        (v1, v2) -> v1,
                        TreeMap::new
                ));
    }

    private boolean isWeekday(Entry<String, Long> entry) {
        LocalDate date = LocalDate.parse(entry.getKey());
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    private String formatDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.format(DateTimeFormatter.ofPattern(DISPLAY_FORMAT));
    }
}
