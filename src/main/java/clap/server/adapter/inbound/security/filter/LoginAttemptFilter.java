package clap.server.adapter.inbound.security.filter;

import clap.server.application.port.inbound.auth.CheckAccountLockStatusUseCase;
import clap.server.exception.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static clap.server.adapter.inbound.security.WebSecurityUrl.LOGIN_ENDPOINT;
import static clap.server.common.utils.ClientIpParseUtil.getClientIp;
import static clap.server.common.utils.ParseNicknameUtil.getNicknameFromRequestBody;


@RequiredArgsConstructor
@Slf4j
public class LoginAttemptFilter extends OncePerRequestFilter {

    private final CheckAccountLockStatusUseCase checkAccountLockStatusUseCase;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (request.getRequestURI().equals(LOGIN_ENDPOINT)) {
                String nickname = request.getParameter("nickname");
                checkAccountLockStatusUseCase.checkAccountIsLocked(nickname);

            }
        } catch (AuthException e) {
            log.warn("Authentication failed for IP: {}. Error: {}", getClientIp(request), e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(e.getErrorCode().getCustomCode());
            log.info("Sent error response: {}", e.getErrorCode().getCustomCode());
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("user", null, new ArrayList<>());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
            byte[] content = cachingRequest.getContentAsByteArray();
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "요청 바디의 내용을 읽을 수 없음";
        }
    }

}
