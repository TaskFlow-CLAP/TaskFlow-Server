package clap.server.config.web;

import clap.server.common.utils.StringToPeriodTypeConverter;
import clap.server.common.utils.StringToStatisticsTypeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        conversionService.setConverters(Set.of(
                new StringToStatisticsTypeConverter(),
                new StringToPeriodTypeConverter()
        ));
        return conversionService;
    }
}
