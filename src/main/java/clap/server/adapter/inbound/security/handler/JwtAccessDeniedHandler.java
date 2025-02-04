package clap.server.adapter.inbound.security.handler;

import clap.server.exception.code.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        AuthErrorCode errorCode = AuthErrorCode.FORBIDDEN;

        response.setStatus(errorCode.getHttpStatus().value());
        response.getWriter().write(errorCode.getCustomCode());
    }
}
