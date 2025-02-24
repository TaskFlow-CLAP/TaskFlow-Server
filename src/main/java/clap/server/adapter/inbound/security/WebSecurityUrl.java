package clap.server.adapter.inbound.security;

public class WebSecurityUrl {
    private WebSecurityUrl() {
        throw new IllegalStateException("Utility class");
    }

    public static final String [] HEALTH_CHECK_ENDPOINT = {"/health"};
    public static final String[] READ_ONLY_PUBLIC_ENDPOINTS = {"/favicon.ico"};
    public static final String LOGIN_ENDPOINT = "/api/auths/login";
    public static final String[] SWAGGER_ENDPOINTS = {
            "/swagger/api-docs/**", "/swagger/v3/api-docs/**",
            "/swagger-ui/**", "/swagger"
    };
    public static final String REISSUANCE_ENDPOINT = "/api/auths/reissuance";
    public static final String PASSWORD_EMAIL_ENDPOINT = "/api/new-password";
    public static final String TEMPORARY_TOKEN_ALLOWED_ENDPOINT = "/api/members/initial-password";
    public static final String[] ANONYMOUS_ENDPOINTS = {LOGIN_ENDPOINT, PASSWORD_EMAIL_ENDPOINT};
}
