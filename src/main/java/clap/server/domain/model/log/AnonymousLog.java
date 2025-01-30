package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogTypeEnum;
import clap.server.domain.model.member.Member;
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

    public static AnonymousLog createAnonymousLog(HttpServletRequest request, HttpServletResponse response, Object result,
                                            LocalDateTime responseAt, LogTypeEnum logType, String customCode, String body, String nickName) {
        return AnonymousLog.builder()
                .serverIp("127.0.0.1")
                .clientIp(request.getRemoteAddr())
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .statusCode(response.getStatus())
                .customStatusCode(customCode)
                .request(body)
                .response(result != null ? result.toString() : "UNKNOWN")
                .requestAt(LocalDateTime.now())
                .responseAt(responseAt)
                .logType(logType)
                .loginNickname(nickName)
                .build();
    }
}