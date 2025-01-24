package clap.server.adapter.inbound.web.dto.statistics;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum StatisticsType {
    REQUEST_BY_PERIOD("request-by-period"),
    PROCESS_BY_PERIOD("process-by-period"),
    REQUEST_BY_CATEGORY("request-by-category"),
    PROCESS_BY_MANAGER("process-by-manager");

    private final String type;

    @JsonCreator
    public static StatisticsType from(String input) {
        for (StatisticsType statisticsType : StatisticsType.values()) {
            if (statisticsType.getType().equals(input)) {
                return statisticsType;
            }
        }
        return null;
    }
}
