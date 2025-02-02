package clap.server.config.web;

import clap.server.common.utils.StringToPeriodTypeConverter;
import clap.server.common.utils.StringToStatisticsTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToStatisticsTypeConverter());
        registry.addConverter(new StringToPeriodTypeConverter());
    }
}
