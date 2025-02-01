package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.common.utils.ClientIpParseUtil;
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
public class MemberLog extends ApiLog {
    private Member member;

    public static MemberLog createMemberLog(HttpServletRequest request, HttpServletResponse response, Object responseResult, LogStatus logStatus, String customCode, String requestBody, Member member) {
        return MemberLog.builder()
                .clientIp(ClientIpParseUtil.getClientIp(request))
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .statusCode(response.getStatus())
                .customStatusCode(customCode)
                .requestBody(requestBody)
                .responseBody(responseResult != null ? responseResult.toString() : "일반 실패")
                .requestAt(LocalDateTime.now())
                .logStatus(logStatus)
                .member(member)
                .build();
    }
}