package clap.server.common.utils;

import clap.server.adapter.inbound.web.dto.statistics.PeriodType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPeriodTypeConverter implements Converter<String, PeriodType> {
    @Override
    public PeriodType convert(String source) {
        return PeriodType.from(source);
    }
}
