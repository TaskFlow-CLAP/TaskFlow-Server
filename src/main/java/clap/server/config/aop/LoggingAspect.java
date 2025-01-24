package clap.server.config.aop;

import clap.server.application.port.outbound.log.ApiLogRepositoryPort;
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
//import org.springframework.security.core.context.SecurityContextHolder;
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

    private final ApiLogRepositoryPort apiLogRepositoryPort;
    private final ObjectMapper objectMapper;

    @Pointcut("execution(* clap.server.adapter.inbound.web..*Controller.*(..)) || execution(* clap.server.adapter.inbound.web..*Controller.*(..))") // TODO : 컨트롤러만 있는 패키지로 수정 예정
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logApiRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = wrapRequest(attributes.getRequest());
        HttpServletResponse response = attributes.getResponse();
        LocalDateTime requestAt = LocalDateTime.now();

        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            LocalDateTime responseAt = LocalDateTime.now();

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String logType = extractLogType(methodSignature);

            //TODO: security filter 구현 후 주석 해제
//            boolean isAuthenticated = isUserAuthenticated();

            String customCode = getCustomCode(response);

            // 로그 저장 로직
            ApiLog.ApiLogBuilder logBuilder = ApiLog.builder()
                    .serverIp("127.0.0.1")
                    .clientIp(request.getRemoteAddr())
                    .requestUrl(request.getRequestURI())
                    .requestMethod(request.getMethod())
                    .statusCode(response != null ? response.getStatus() : 500)
                    .customStatusCode(customCode)
                    .request(extractRequestBody(request))
                    .response(result != null ? result.toString() : "UNKNOWN")
                    .requestAt(requestAt)
                    .responseAt(responseAt)
                    .logType(logType);

            //TODO: security filter 구현 후 주석 해제
            if ("로그인 시도".equals(logType)) {
                String nickname = extractNicknameFromRequestBody(request);
                logBuilder.memberId(nickname); // 비회원 로그
//            } else if (isAuthenticated) {
//                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                if (principal instanceof clap.server.adapter.outbound.persistense.entity.member.MemberEntity member) {
//                    logBuilder.memberId(String.valueOf(member.getMemberId())); // 회원 로그
//                }
            }

            apiLogRepositoryPort.save(logBuilder.build());
        }
        return result;
    }

    private HttpServletRequest wrapRequest(HttpServletRequest request) {
        // ContentCachingRequestWrapper로 래핑
        if (request instanceof ContentCachingRequestWrapper) {
            return request;
        }
        return new ContentCachingRequestWrapper(request);
    }

    private String extractLogType(MethodSignature methodSignature) {
        // LogType 어노테이션에서 로그 타입 추출
        if (methodSignature.getMethod().isAnnotationPresent(LogType.class)) {
            return methodSignature.getMethod().getAnnotation(LogType.class).value();
        }
        return "일반 요청"; // 기본값
    }

    private String getCustomCode(HttpServletResponse response) {
        // ErrorContext에서 커스텀 코드 추출
        String customCode = ErrorContext.getCustomCode();
        return customCode != null ? customCode : "CUSTOM" + (response != null ? response.getStatus() : 500);
    }

    private String extractNicknameFromRequestBody(HttpServletRequest request) {
        try {
            // 요청 본문에서 nickname 필드 추출
            String requestBody = extractRequestBody(request);
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            return jsonNode.has("nickname") ? jsonNode.get("nickname").asText() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractRequestBody(HttpServletRequest request) {
        try {
            // ContentCachingRequestWrapper를 통해 요청 본문 읽기
            ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
            byte[] content = cachingRequest.getContentAsByteArray();
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "ERROR: Unable to read request body";
        }
    }

    //TODO: security filter 구현 후 주석 해제
//    private boolean isUserAuthenticated() {
//        // 사용자 인증 상태 확인
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && authentication.isAuthenticated()
//                && !"anonymousUser".equals(authentication.getPrincipal());
//    }
}
