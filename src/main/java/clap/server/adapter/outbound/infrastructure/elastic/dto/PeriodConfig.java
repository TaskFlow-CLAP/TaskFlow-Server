package clap.server.adapter.outbound.infrastructure.elastic.dto;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;

public record PeriodConfig(
        long daysToSubtract,
        CalendarInterval calendarInterval,
        int substringStart,
        int substringEnd
) {}