package clap.server.application.port.outbound.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import jakarta.servlet.http.HttpServletRequest;

public interface LoggingPort {
    void createAnonymousLog(HttpServletRequest request, int statusCode, String customCode, LogStatus logStatus, Object responseBody, String requestBody, String nickName);
    void createMemberLog(HttpServletRequest request, int statusCode, String customCode,LogStatus logStatus, Object responseBody, String requestBody, Long memberId);
    void createLoginFailedLog(HttpServletRequest request, int statusCode, String customCode, LogStatus logStatus, String requestBody, String nickName);
}