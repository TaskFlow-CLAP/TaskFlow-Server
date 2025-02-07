package clap.server.config.aop;

import clap.server.adapter.inbound.security.service.SecurityUserDetails;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.domain.LogService;
import clap.server.common.annotation.log.LogType;
import clap.server.exception.BaseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private final ObjectMapper objectMapper;
    private final LogService logService;

    @Pointcut("execution(* clap.server.adapter.inbound.web..*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logApiRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        Object result = null;
        Exception capturedException = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            capturedException = ex;
            throw ex;
        } finally {
            LogStatus logStatus = getLogType((MethodSignature) joinPoint.getSignature());
            int statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
            String customCode = null;

            if (capturedException != null) {
                if (capturedException instanceof BaseException e) {
                    statusCode = e.getCode().getHttpStatus().value();
                    customCode = e.getCode().getCustomCode();
                }
            } else {
                statusCode = response.getStatus();
            }

            if (logStatus != null) {
                if (LogStatus.LOGIN.equals(logStatus)) {
                    handleLoginLog(statusCode, request, customCode, logStatus, result);

                } else {
                    if (!isUserAuthenticated()) {
                        log.error("로그인 시도 로그를 기록할 수 없음");
                    } else {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal instanceof SecurityUserDetails userDetails) {
                            logService.createMemberLog(request, statusCode, customCode, logStatus, result, getRequestBody(request), userDetails.getUserId());
                        }
                    }
                }
            }
        }
        return result;
    }

    private void handleLoginLog(int statusCode, HttpServletRequest request, String customCode, LogStatus logStatus, Object result) throws JsonProcessingException {
        if (statusCode == HttpStatus.SC_OK) {
            logService.createAnonymousLog(request, statusCode, customCode, logStatus, result, getRequestBody(request), getNicknameFromRequestBody(request));
        } else {
            logService.createLoginFailedLog(request, statusCode, customCode, logStatus, getRequestBody(request), getNicknameFromRequestBody(request));
        }
    }

    private LogStatus getLogType(MethodSignature methodSignature) {
        if (methodSignature.getMethod().isAnnotationPresent(LogType.class)) {
            return methodSignature.getMethod().getAnnotation(LogType.class).value();
        } else {
            return null;
        }
    }

    private String getNicknameFromRequestBody(HttpServletRequest request) {
        try {
            String requestBody = getRequestBody(request);
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            return jsonNode.has("nickname") ? jsonNode.get("nickname").asText() : null;
        } catch (Exception e) {
            return null;
        }
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

    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
