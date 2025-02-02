package clap.server.adapter.inbound.web.dto.statistics;

import clap.server.exception.StatisticsException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static clap.server.exception.code.StatisticsErrorCode.STATISTICS_BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum PeriodType {
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private final String type;

    @JsonCreator
    public static PeriodType from(String input) {
        for (PeriodType periodType : PeriodType.values()) {
            if (periodType.getType().equals(input)) {
                return periodType;
            }
        }
        throw new StatisticsException(STATISTICS_BAD_REQUEST);
    }
}
