package clap.server.config.security;

import clap.server.common.properties.ServerDomainProperties;
import clap.server.common.properties.WebDomainProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static clap.server.common.constants.AuthConstants.SESSION_ID;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {
    private final ServerDomainProperties serverDomainProperties;
    private final WebDomainProperties webDomainProperties;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                List.of(serverDomainProperties.getLocal(), serverDomainProperties.getService(),
                        webDomainProperties.getLocal(), webDomainProperties.getService()));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION, SESSION_ID.getValue()));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
