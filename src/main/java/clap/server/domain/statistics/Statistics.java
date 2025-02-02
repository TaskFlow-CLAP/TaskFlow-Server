package clap.server.domain.statistics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistics {
    public static Map<String, Long> transformToWeekdayStatistics(Map<String, Long> statistics) {
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
