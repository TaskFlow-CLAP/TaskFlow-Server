package clap.server.config.security;

public class WebSecurityUrl {
    private WebSecurityUrl() {
        throw new IllegalStateException("Utility class");
    }

    protected static final String [] HEALTH_CHECK_ENDPOINT = {"/health"};
    protected static final String[] READ_ONLY_PUBLIC_ENDPOINTS = {"/favicon.ico"};
    protected static final String[] AUTHENTICATED_ENDPOINTS = {};
    protected static final String[] ANONYMOUS_ENDPOINTS = {"/api/auths/login"};
    protected static final String[] SWAGGER_ENDPOINTS = {
            "/swagger/api-docs/**", "/swagger/v3/api-docs/**",
            "/swagger-ui/**", "/swagger"
    };
    protected static final String[] REISSUANCE_ENDPOINTS = {"/api/auths/reissuance"};
}
