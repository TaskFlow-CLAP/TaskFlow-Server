package clap.server.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import static clap.server.config.security.WebSecurityUrl.*;

@Configuration
@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityAdapterConfig securityAdapterConfig;
    private final CorsConfigurationSource corsConfigurationSource;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Profile({"local", "dev"})
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChainForDev(HttpSecurity http) throws Exception {
        return defaultSecurity(http)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(
                        auth ->
                                defaultAuthorizeHttpRequest(auth)
                                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                                        .anyRequest().authenticated()
                ).build();
    }

    @Bean
    @Profile({"prod"})
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChainForProd(HttpSecurity http) throws Exception {
        return defaultSecurity(http)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(auth -> defaultAuthorizeHttpRequest(auth).anyRequest().authenticated()
                ).build();
    }

    private HttpSecurity defaultSecurity(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .with(securityAdapterConfig, Customizer.withDefaults())
                .exceptionHandling(
                        exception -> exception
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint)
                );
    }

    private AbstractRequestMatcherRegistry<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl> defaultAuthorizeHttpRequest(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth
    ) {
        return auth
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "*").permitAll()
                .requestMatchers(HttpMethod.GET, READ_ONLY_PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(HEALTH_CHECK_ENDPOINT).permitAll()
                .requestMatchers(REISSUANCE_ENDPOINTS).permitAll()
                .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                .requestMatchers(ANONYMOUS_ENDPOINTS).permitAll();
    }

}
