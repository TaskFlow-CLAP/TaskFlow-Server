package clap.server.application.service.log;

import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.admin.MemberLogResponse;
import clap.server.adapter.outbound.persistense.ApiLogPersistenceAdapter;
import clap.server.application.mapper.response.LogMapper;
import clap.server.application.port.inbound.domain.LoginDomainService;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindApiLogsService implements FindApiLogsUsecase {

    private final ApiLogPersistenceAdapter apiLogPersistenceAdapter;
    private final LoginDomainService loginDomainService;

    @Override
    public List<AnonymousLogResponse> getAnonymousLogs() {
        return apiLogPersistenceAdapter.findAnonymousLogs().stream()
                .map(anonymousLog -> {
                    int failedAttempts = loginDomainService.getFailedAttemptCount(anonymousLog.getLoginNickname());
                    return LogMapper.toAnonymounsLogResponse(anonymousLog, failedAttempts);
                })
                .toList();
    }

    //TODO: Paging으로 수정
    @Override
    public List<MemberLogResponse> getMemberLogs() {
        return apiLogPersistenceAdapter.findMemberLogs().stream()
                .map(LogMapper::toMemberLogResponse)
                .toList();
    }

    //테스트용
    @Override
    public List<ApiLog> getApiLogs() {
        return apiLogPersistenceAdapter.findAllLogs();
    }
}
