package clap.server.adapter.inbound.security.filter;

import clap.server.adapter.outbound.jwt.JwtClaims;
import clap.server.adapter.outbound.jwt.access.AccessTokenClaimKeys;
import clap.server.application.port.outbound.auth.JwtProvider;
import clap.server.application.port.outbound.auth.forbidden.ForbiddenTokenPort;
import clap.server.exception.JwtException;
import clap.server.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static clap.server.adapter.inbound.security.WebSecurityUrl.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService securityUserDetailsService;
    private final JwtProvider accessTokenProvider;
    private final JwtProvider temporaryTokenProvider;
    private final AccessDeniedHandler accessDeniedHandler;
    private final ForbiddenTokenPort forbiddenTokenPort;

    public static final String[] PUBLIC_ENDPOINTS = Stream.of(
            HEALTH_CHECK_ENDPOINT,
            READ_ONLY_PUBLIC_ENDPOINTS,
            SWAGGER_ENDPOINTS
    ).flatMap(Arrays::stream).toArray(String[]::new);

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (isAnonymousRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = resolveAccessToken(request);

            UserDetails userDetails = getUserDetails(accessToken);
            authenticateUser(userDetails, request);
        } catch (AccessDeniedException e) {
            accessDeniedHandler.handle(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isAnonymousRequest(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        boolean isAnonymousURI = Arrays.stream(ANONYMOUS_ENDPOINTS)
                .anyMatch(endpoint -> new AntPathMatcher().match(endpoint, requestUri));
        boolean isAnonymous = request.getHeader(HttpHeaders.AUTHORIZATION) == null;
        return (isAnonymousURI && isAnonymous) || requestUri.equals(REISSUANCE_ENDPOINT);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(endpoint -> new AntPathMatcher().match(endpoint, request.getRequestURI()));
    }


    private String resolveAccessToken(
            HttpServletRequest request
    ) throws ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = accessTokenProvider.resolveToken(authHeader);

        if (!StringUtils.hasText(accessToken)) {
            log.error("EMPTY_ACCESS_TOKEN");
            handleAuthException(AuthErrorCode.EMPTY_ACCESS_KEY);
        }

        String requestUrl = request.getRequestURI();
        boolean isTemporaryToken = isTemporaryToken(accessToken);
        JwtProvider tokenProvider = isTemporaryToken ? temporaryTokenProvider : accessTokenProvider;

        log.info("Token is Temporary {}", isTemporaryToken);

        if (isTemporaryTokenAllowed(requestUrl) != isTemporaryToken) {
            log.error("FORBIDDEN_TEMPORARY_TOKEN_ACCESS");
            handleAuthException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);
        }

        if (forbiddenTokenPort.getIsForbidden(accessToken)) {
            log.error("FORBIDDEN_ACCESS_TOKEN");
            handleAuthException(AuthErrorCode.FORBIDDEN_ACCESS_TOKEN);
        }

        if (tokenProvider.isTokenExpired(accessToken)) {
            log.error("EXPIRED_TOKEN");
            handleAuthException(AuthErrorCode.EXPIRED_TOKEN);
        }

        return accessToken;
    }




    private boolean isTemporaryTokenAllowed(String requestUrl) {
        return requestUrl.equals(TEMPORARY_TOKEN_ALLOWED_ENDPOINT);
    }

    private boolean isTemporaryToken(String token) {
        try {
            Claims claims = temporaryTokenProvider.getClaimsFromToken(token);
            return claims.get("isTemporary", Boolean.class) != null && claims.get("isTemporary", Boolean.class);
        } catch (Exception e) {
            return false;
        }
    }

    private UserDetails getUserDetails(String accessToken) {
        JwtProvider tokenProvider = isTemporaryToken(accessToken) ? temporaryTokenProvider : accessTokenProvider;
        JwtClaims claims = tokenProvider.parseJwtClaimsFromToken(accessToken);
        String memberId = (String) claims.getClaims().get(AccessTokenClaimKeys.USER_ID.getValue());
        return securityUserDetailsService.loadUserByUsername(memberId);
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private void handleAuthException(AuthErrorCode authErrorCode) throws ServletException {
        JwtException exception = new JwtException(authErrorCode);
        throw new ServletException(exception);
    }
}
