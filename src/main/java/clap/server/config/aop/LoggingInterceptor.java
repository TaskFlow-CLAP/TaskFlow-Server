package clap.server.config.aop;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import static clap.server.exception.ErrorContext.getCustomCode;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        System.out.println("Interceptor");
        System.out.println(new String(cachingRequest.getContentAsByteArray()));
        int statusCode = response.getStatus();
        String customCode = getCustomCode();
        //TODO: 여기서 error에 대한 log 저장
        log.info("API 응답 예외 발생 - Status: {}, CustomCode: {}", statusCode, customCode);
    }
}