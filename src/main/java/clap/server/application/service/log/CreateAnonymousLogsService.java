package clap.server.application.service.log;

import clap.server.adapter.outbound.persistense.ApiLogPersistenceAdapter;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.application.port.inbound.log.CreateAnonymousLogsUsecase;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.AnonymousLog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
public class CreateAnonymousLogsService implements CreateAnonymousLogsUsecase {
    private final CommandLogPort commandLogPort;

    @Override
    public void createAnonymousLog(HttpServletRequest request,  int statusCode, String customCode, LogStatus logStatus, Object responseBody, String requestBody, String nickName) {
        AnonymousLog anonymousLog = AnonymousLog.createAnonymousLog(request, statusCode,customCode, logStatus, responseBody, requestBody, nickName);
        commandLogPort.saveAnonymousLog(anonymousLog);
    }
}
