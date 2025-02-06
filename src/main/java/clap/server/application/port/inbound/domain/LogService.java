package clap.server.application.port.inbound.domain;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.outbound.auth.loginLog.LoadLoginLogPort;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.common.utils.ClientIpParseUtil;
import clap.server.domain.model.auth.LoginLog;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;
import clap.server.domain.model.member.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final LoadLoginLogPort loadLoginLogPort;
    private final ObjectMapper objectMapper;

    public void createAnonymousLog(HttpServletRequest request,  int statusCode, String customCode, LogStatus logStatus, Object responseBody, String requestBody, String nickName) {
        AnonymousLog anonymousLog = AnonymousLog.createAnonymousLog(request, statusCode,customCode, logStatus, responseBody, requestBody, nickName);
        commandLogPort.saveAnonymousLog(anonymousLog);
    }

    public void createMemberLog(HttpServletRequest request, int statusCode, String customCode,LogStatus logStatus, Object responseBody, String requestBody, Long userId) {
        Member member = memberService.findById(userId);
        MemberLog memberLog = MemberLog.createMemberLog(request, statusCode, customCode, logStatus,  responseBody, requestBody, member);
        commandLogPort.saveMemberLog(memberLog);
    }

    public void createLoginFailedLog(HttpServletRequest request, int statusCode, String customCode, LogStatus logStatus, String requestBody, String nickName) throws JsonProcessingException {
        LoginLog loginLog = loadLoginLogPort.findByClientIp(ClientIpParseUtil.getClientIp(request)).orElse(null);
        String responseBody = loginLog != null ? loginLog.toSummaryString() : null;
        AnonymousLog anonymousLog = AnonymousLog.createAnonymousLog(request, statusCode,customCode, logStatus, responseBody, requestBody, nickName);
        commandLogPort.saveAnonymousLog(anonymousLog);
    }
}
