package clap.server.adapter.inbound.security.filter;

import clap.server.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (!response.isCommitted()) {
                JwtException exception = JwtErrorCodeUtil.determineAuthErrorException(e);
                sendAuthError(response, exception);
            }
        }
    }

    private void sendAuthError(HttpServletResponse response, JwtException e) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(e.getErrorCode().getHttpStatus().value());
            response.getWriter().write(e.getErrorCode().getCustomCode());
        }
    }

}
