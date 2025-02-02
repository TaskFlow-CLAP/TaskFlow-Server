package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.common.utils.ClientIpParseUtil;
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

    public static AnonymousLog createAnonymousLog(HttpServletRequest request, int statusCode, String customCode, LogStatus logStatus, Object responseBody, String requestBody, String nickName) {
        return AnonymousLog.builder()
                .clientIp(ClientIpParseUtil.getClientIp(request))
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .statusCode(statusCode)
                .customStatusCode(customCode != null ? customCode : "")
                .requestBody(requestBody)
                .responseBody(responseBody != null ? responseBody.toString() : "로그인 실패")
                .requestAt(LocalDateTime.now())
                .logStatus(logStatus)
                .loginNickname(nickName)
                .build();
    }
}