package clap.server.application.port.inbound.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

public interface CreateAnonymousLogsUsecase {
    void createAnonymousLog(HttpServletRequest request, int statusCode, LogStatus logStatus,  Object responseBody, String requestBody, String nickName);

}
