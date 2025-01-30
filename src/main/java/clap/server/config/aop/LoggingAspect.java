package clap.server.config.aop;

import clap.server.adapter.inbound.security.SecurityUserDetails;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogTypeEnum;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.config.annotation.LogType;
import clap.server.domain.model.log.ApiLog;
import clap.server.exception.ErrorContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final CommandLogPort commandLogPort;
    private final ObjectMapper objectMapper;

    @Pointcut("execution(* clap.server.adapter.inbound.web..*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logApiRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = wrapRequest(attributes.getRequest());
        HttpServletResponse response = attributes.getResponse();

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            LocalDateTime responseAt = LocalDateTime.now();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            LogTypeEnum logType = getLogType(methodSignature);
            String customCode = getCustomCode(response);
            String userId = null;

            if (LogTypeEnum.LOGIN.equals(logType)) {
                userId = getNicknameFromRequestBody(request);
                saveApiLog(request, response, result, responseAt, logType, customCode, userId);
            } else if (LogTypeEnum.GENERAL.equals(logType)) {
                if (!isUserAuthenticated()) {
                    log.error("인증된 사용자가 아니기 때문에 로그를 기록할 수 없습니다.");
                }
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof SecurityUserDetails userDetails) {
                    saveApiLog(request, response, result, responseAt, logType, customCode, userId);
                }
            }
        }
        return result;
    }

    private void saveApiLog(HttpServletRequest request, HttpServletResponse response,
                            Object result, LocalDateTime responseAt, LogTypeEnum logType,
                            String customCode, String userId) {
        ApiLog apiLog = ApiLog.builder()
                .serverIp("127.0.0.1")
                .clientIp(request.getRemoteAddr())
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .statusCode(response.getStatus())
                .customStatusCode(customCode)
                .request(getRequestBody(request))
                .response(result != null ? result.toString() : "UNKNOWN")
                .requestAt(LocalDateTime.now())
                .responseAt(responseAt)
                .logType(logType)
                .userId(userId)
                .build();
        commandLogPort.save(apiLog);
    }


    private HttpServletRequest wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return request;
        }
        return new ContentCachingRequestWrapper(request);
    }

    private LogTypeEnum getLogType(MethodSignature methodSignature) {
        if (methodSignature.getMethod().isAnnotationPresent(LogType.class)) {
            return LogTypeEnum.fromDescription(methodSignature.getMethod().getAnnotation(LogType.class).value());
        } else {
            throw new IllegalArgumentException("Log 추적이 허용되지 않은 엔드포인트");
        }
    }

    private String getCustomCode(HttpServletResponse response) {
        String customCode = ErrorContext.getCustomCode();
        return customCode != null ? customCode : "CUSTOM" + (response != null ? response.getStatus() : 500);
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
            return "ERROR: Unable to read request body";
        }
    }

    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
