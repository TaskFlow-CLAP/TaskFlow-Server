package clap.server.application.port.inbound.domain;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;
import clap.server.domain.model.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {
    private final CommandLogPort commandLogPort;
    private final MemberService memberService;

    public void createAnonymousLog(HttpServletRequest request,  int statusCode, String customCode, LogStatus logStatus, Object responseBody, String requestBody, String nickName) {
        AnonymousLog anonymousLog = AnonymousLog.createAnonymousLog(request, statusCode,customCode, logStatus, responseBody, requestBody, nickName);
        commandLogPort.saveAnonymousLog(anonymousLog);
    }

    public void createMemberLog(HttpServletRequest request, int statusCode, String customCode,LogStatus logStatus, Object responseBody, String requestBody, Long userId) {
        Member member = memberService.findById(userId);
        MemberLog memberLog = MemberLog.createMemberLog(request, statusCode, customCode, logStatus,  responseBody, requestBody, member);
        commandLogPort.saveMemberLog(memberLog);
    }
}
