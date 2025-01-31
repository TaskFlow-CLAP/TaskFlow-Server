package clap.server.application.service.log;

import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.adapter.outbound.persistense.ApiLogPersistenceAdapter;
import clap.server.application.mapper.response.LogMapper;
import clap.server.application.port.inbound.domain.LoginDomainService;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.application.port.outbound.log.LoadLogPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindApiLogsService implements FindApiLogsUsecase {

    private final ApiLogPersistenceAdapter apiLogPersistenceAdapter;
    private final LoginDomainService loginDomainService;
    private final LoadLogPort loadLogPort;

    @Override
    public Page<AnonymousLogResponse> filterAnonymousLogs(FilterLogRequest anonymousLogRequest, Pageable pageable) {
        Page<AnonymousLog> anonymousLogs = loadLogPort.filterAnonymousLogs(anonymousLogRequest, pageable);
        return anonymousLogs.map(anonymousLog -> {
            int failedAttempts = loginDomainService.getFailedAttemptCount(anonymousLog.getLoginNickname());
            return LogMapper.toAnonymounsLogResponse(anonymousLog, failedAttempts);
        });
    }

    @Override
    public Page<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable) {
        return loadLogPort.filterMemberLogs(memberLogRequest, pageable);
    }

    //테스트용
    @Override
    public List<ApiLog> getApiLogs() {
        return apiLogPersistenceAdapter.findAllLogs();
    }
}
