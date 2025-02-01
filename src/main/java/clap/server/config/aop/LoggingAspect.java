package clap.server.config.aop;

import clap.server.adapter.inbound.security.SecurityUserDetails;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.log.CreateAnonymousLogsUsecase;
import clap.server.application.port.inbound.log.CreateMemberLogsUsecase;
import clap.server.config.annotation.LogType;
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
    private final ObjectMapper objectMapper;
    private final CreateAnonymousLogsUsecase createAnonymousLogsUsecase;
    private final CreateMemberLogsUsecase createMemberLogsUsecase;

    @Pointcut("execution(* clap.server.adapter.inbound.web..*Controller.*(..))")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logApiRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            log.error("Exception occurred: {}", ex.getMessage());
            log.info("response.getStatus()={}",response.getStatus());
            log.info("getRequestBody()={}", getRequestBody(request));
            throw ex;
        } finally {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            LogStatus logType = getLogType(methodSignature);
            String customCode = getCustomCode(response);
            if (logType != null) {
                if (LogStatus.LOGIN.equals(logType)) {
                    log.info("result={}",result);
                    log.info("response.getStatus()={}",response.getStatus());
                    log.info("getRequestBody()={}", getRequestBody(request));
                    log.info("getNicknameFromRequestBody()={}", getNicknameFromRequestBody(request));
                    createAnonymousLogsUsecase.createAnonymousLog(request, response, result, logType, customCode, getRequestBody(request), getNicknameFromRequestBody(request));
                } else {
                    if (!isUserAuthenticated()) {
                        log.error("로그인 시도 로그를 기록할 수 없음");
                    } else {
                        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (principal instanceof SecurityUserDetails userDetails) {
                            createMemberLogsUsecase.createMemberLog(request, response, result, logType, customCode, getRequestBody(request), userDetails.getUserId());
                        }
                    }
                }
            }
        }
        return result;
    }

    private LogStatus getLogType(MethodSignature methodSignature) {
        if (methodSignature.getMethod().isAnnotationPresent(LogType.class)) {
            return methodSignature.getMethod().getAnnotation(LogType.class).value();
        } else {
            return null;
        }
    }

    //TODO: 로그인 시도에 대한 에러 잡도록 수정
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
            return "요청 바디의 내용을 읽을 수 없음";
        }
    }

    private boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
