package clap.server.application.service.log;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
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
import clap.server.domain.model.log.MemberLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindApiLogsService implements FindApiLogsUsecase {

    private final LoginDomainService loginDomainService;
    private final LoadLogPort loadLogPort;

    @Override
    public PageResponse<AnonymousLogResponse> filterAnonymousLogs(FilterLogRequest anonymousLogRequest, Pageable pageable) {
        Page<AnonymousLog> anonymousLogs = loadLogPort.filterAnonymousLogs(anonymousLogRequest, pageable);
        Page<AnonymousLogResponse> anonymousLogResponses = anonymousLogs.map(anonymousLog -> {
            int failedAttempts = loginDomainService.getFailedAttemptCount(anonymousLog.getLoginNickname());
            return LogMapper.toAnonymousLogResponse(anonymousLog, failedAttempts);
        });
        return PageResponse.from(anonymousLogResponses);
    }

    @Override
    public PageResponse<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable) {
        Page<MemberLog> memberLogs = loadLogPort.filterMemberLogs(memberLogRequest, pageable);
        Page<MemberLogResponse> memberLogResponses = memberLogs.map(LogMapper::toMemberLogResponse);
        return PageResponse.from(memberLogResponses);
    }
}
