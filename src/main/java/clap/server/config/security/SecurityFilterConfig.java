package clap.server.config.security;

import clap.server.adapter.inbound.security.LoginAttemptFilter;
import clap.server.adapter.inbound.security.filter.JwtAuthenticationFilter;
import clap.server.adapter.inbound.security.filter.JwtExceptionFilter;
import clap.server.application.port.outbound.auth.JwtProvider;
import clap.server.application.service.auth.LoginAttemptService;
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
	private final LoginAttemptService loginAttemptService;
	private final JwtProvider accessTokenProvider;
	private final JwtProvider temporaryTokenProvider;
	private final AccessDeniedHandler accessDeniedHandler;

	@Bean
	public JwtExceptionFilter jwtExceptionFilter() {
		return new JwtExceptionFilter();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(securityUserDetails, accessTokenProvider, temporaryTokenProvider, accessDeniedHandler);
	}

	@Bean
	public LoginAttemptFilter loginAttemptFilter() {
		return new LoginAttemptFilter(loginAttemptService);
	}
}
