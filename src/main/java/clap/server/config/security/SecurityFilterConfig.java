package clap.server.config.security;

import clap.server.application.port.outbound.auth.JwtProvider;
import clap.server.adapter.inbound.security.filter.JwtAuthenticationFilter;
import clap.server.adapter.inbound.security.filter.JwtExceptionFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SecurityFilterConfig {
	private final UserDetailsService securityUserDetails;
	private final JwtProvider accessTokenProvider;
	private final AccessDeniedHandler accessDeniedHandler;

	@Bean
	public JwtExceptionFilter jwtExceptionFilter() {
		return new JwtExceptionFilter();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(securityUserDetails, accessTokenProvider, accessDeniedHandler);
	}
}
