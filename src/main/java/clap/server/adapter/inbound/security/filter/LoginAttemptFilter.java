package clap.server.adapter.inbound.security.filter;

import clap.server.application.service.auth.LoginAttemptService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

import static clap.server.adapter.inbound.security.WebSecurityUrl.LOGIN_ENDPOINT;
import static clap.server.common.utils.ClientIpParseUtil.getClientIp;


@RequiredArgsConstructor
@Slf4j
public class LoginAttemptFilter extends OncePerRequestFilter {

    private final LoginAttemptService loginAttemptService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //TODO: 25.02.04 요구사항 변경에 따른 임시 주석 처리
//        String sessionId = request.getHeader(SESSION_ID.getValue().toLowerCase());

        if (request.getRequestURI().equals(LOGIN_ENDPOINT)) {

//            if (sessionId == null) {
//                throw new AuthException(GlobalErrorCode.BAD_REQUEST);
//            }
            String clientIp = getClientIp(request);
            loginAttemptService.checkAccountIsLocked(clientIp);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("user", null, new ArrayList<>());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
