package clap.server.domain.policy.task;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

class TaskStatisticsPolicyTest {
    private final TaskStatisticsPolicy taskStatisticsPolicy = new TaskStatisticsPolicy();

    @Test
    void filterAndFormatWeekdayStatistics() {
        //given
        Map<String, Long> statistics = new TreeMap<>();
        statistics.put("2025-02-01", 8L);
        statistics.put("2025-01-30", 7L);
        statistics.put("2025-01-31", 6L);

        Map<String, Long> formattedStatistics = new TreeMap<>();
        formattedStatistics.put("1월 30일", 7L);
        formattedStatistics.put("1월 31일", 6L);

        //when
        Map<String, Long> weekdayStatistics = taskStatisticsPolicy.filterAndFormatWeekdayStatistics(statistics);

        //then
        assertThat(weekdayStatistics).isEqualTo(formattedStatistics);
    }
}