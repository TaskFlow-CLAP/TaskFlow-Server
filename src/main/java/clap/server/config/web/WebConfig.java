package clap.server.config.web;

import clap.server.adapter.inbound.xss.XssPreventionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class WebConfig {

    @Bean
    public FilterRegistrationBean<XssPreventionFilter> xssPreventionFilterRegistrationBean() {
        FilterRegistrationBean<XssPreventionFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssPreventionFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}