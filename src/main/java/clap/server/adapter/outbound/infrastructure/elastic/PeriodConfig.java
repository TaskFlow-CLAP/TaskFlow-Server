package clap.server.adapter.outbound.infrastructure.elastic;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PeriodConfig {
    DAY(1, CalendarInterval.Hour, 11, 19),
    WEEK(14, CalendarInterval.Day, 0, 10);

    private final long daysToSubtract;
    private final CalendarInterval calendarInterval;
    private final int substringStart;
    private final int substringEnd;

}
