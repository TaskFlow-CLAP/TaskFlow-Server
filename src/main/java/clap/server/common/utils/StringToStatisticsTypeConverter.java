package clap.server.common.utils;

import clap.server.adapter.inbound.web.dto.statistics.StatisticsType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatisticsTypeConverter implements Converter<String, StatisticsType> {
    @Override
    public StatisticsType convert(String source) {
        return StatisticsType.from(source);
    }
}
