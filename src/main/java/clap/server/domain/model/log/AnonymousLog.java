package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class AnonymousLog extends ApiLog {
    private String loginNickname;

    public static AnonymousLog createAnonymousLog(HttpServletRequest request, HttpServletResponse response, Object responseResult, LogStatus logStatus, String customCode, String requestBody, String nickName) {
        return AnonymousLog.builder()
                .clientIp(request.getRemoteAddr())
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .statusCode(response.getStatus())
                .customStatusCode(customCode)
                .requestBody(requestBody)
                .responseBody(responseResult != null ? responseResult.toString() : "UNKNOWN")
                .requestAt(LocalDateTime.now())
                .logStatus(logStatus)
                .loginNickname(nickName)
                .build();
    }
}