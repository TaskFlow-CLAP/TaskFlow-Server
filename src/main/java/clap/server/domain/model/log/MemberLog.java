package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogTypeEnum;
import clap.server.adapter.outbound.persistense.entity.task.constant.TaskStatus;
import clap.server.domain.model.member.Member;
import clap.server.domain.model.task.Category;
import clap.server.domain.model.task.Task;
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

    public static MemberLog createMemberLog(HttpServletRequest request, HttpServletResponse response, Object result,
                                            LocalDateTime responseAt, LogTypeEnum logType, String customCode, String body, Member member) {
        return MemberLog.builder()
                .serverIp("127.0.0.1") //TODO: 실제 서버 ip 동적 주입
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
                .member(member)
                .build();
    }
}