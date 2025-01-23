package clap.server.adapter.outbound.infrastructure.elastic;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@RequiredArgsConstructor
public enum PeriodConfig {
    DAY(1, CalendarInterval.Hour, 11, 16),
    WEEK(7, CalendarInterval.Day, 0, 10),
    MONTH(-1, CalendarInterval.Day, 0, 10);

    private final long daysToSubtract;
    private final CalendarInterval calendarInterval;
    private final int substringStart;
    private final int substringEnd;

    // MONTH에 대해 동적으로 daysToSubtract를 계산하는 메서드
    public long getDaysToSubtract() {
        if (this == MONTH) {
            return ChronoUnit.DAYS.between(LocalDate.now().minusMonths(1), LocalDate.now());
        }
        return daysToSubtract;
    }

}
