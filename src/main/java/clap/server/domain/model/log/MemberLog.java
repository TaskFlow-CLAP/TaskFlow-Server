package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.common.utils.ClientIpParseUtil;
import clap.server.domain.model.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class MemberLog extends ApiLog {
    private Member member;

    public static MemberLog createMemberLog(HttpServletRequest request, int statusCode, String customCode, LogStatus logStatus, Object responseBody, String requestBody, Member member) {
        return MemberLog.builder()
                .clientIp(ClientIpParseUtil.getClientIp(request))
                .requestUrl(request.getRequestURI())
                .requestMethod(request.getMethod())
                .statusCode(statusCode)
                .customStatusCode(customCode != null ? customCode : "")
                .requestBody(requestBody)
                .responseBody(responseBody != null ? responseBody.toString() : logStatus.getDescription() + " 실패")
                .requestAt(LocalDateTime.now())
                .logStatus(logStatus)
                .member(member)
                .build();
    }
}