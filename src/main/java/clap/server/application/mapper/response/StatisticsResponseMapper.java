package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsResponse;

import java.util.List;
import java.util.Map;

public class StatisticsResponseMapper {
    private StatisticsResponseMapper() {
        throw new IllegalArgumentException("Utility class");
    }

    public static List<StatisticsResponse> toStatisticsResponse(Map<String, Long> map){
        return map.entrySet()
            .stream()
            .map(result -> new StatisticsResponse(result.getKey(), result.getValue()))
            .toList();
    }
}
