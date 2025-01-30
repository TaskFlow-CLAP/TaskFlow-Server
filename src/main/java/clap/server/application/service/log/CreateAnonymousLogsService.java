package clap.server.application.service.log;

import clap.server.adapter.outbound.persistense.ApiLogPersistenceAdapter;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogTypeEnum;
import clap.server.application.port.inbound.log.CreateAnonymousLogsUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.AnonymousLog;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@ApplicationService
@RequiredArgsConstructor
public class CreateAnonymousLogsService implements CreateAnonymousLogsUsecase {
    private final ApiLogPersistenceAdapter apiLogPersistenceAdapter;

    @Override
    public void createAnonymousLog(HttpServletRequest request, HttpServletResponse response, Object result, LocalDateTime responseAt, LogTypeEnum logType, String customCode, String body, String nickName) {
        AnonymousLog anonymousLog = AnonymousLog.createAnonymousLog(request, response, result, responseAt, logType, customCode, body, nickName);
        apiLogPersistenceAdapter.saveAnonymousLog(anonymousLog);
    }
}
