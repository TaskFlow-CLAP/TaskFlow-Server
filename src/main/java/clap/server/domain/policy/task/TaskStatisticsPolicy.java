package clap.server.domain.policy.task;

import clap.server.common.annotation.architecture.Policy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Policy
public class TaskStatisticsPolicy {
    private static final String DISPLAY_FORMAT = "M월 d일";

    public Map<String, Long> formatStatistics(Map<String, Long> statistics) {
        return statistics.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> formatDate(entry.getKey()),
                        Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    private String formatDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.format(DateTimeFormatter.ofPattern(DISPLAY_FORMAT));
    }

    public Map<String, Long> formatDayStatistics(Map<String, Long> statistics) {
        for (int i = 0; i <= 23; i++) statistics.putIfAbsent(String.format("%02d", i), 0L);

        return statistics.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()) + "시",
                        Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }
}
