package clap.server.application.service.log;

import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.admin.MemberLogResponse;
import clap.server.application.mapper.AnonymousLogMapper;
import clap.server.application.mapper.MemberLogMapper;
import clap.server.application.port.inbound.domain.LoginDomainService;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.ApiLog;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
public class FindApiLogsService implements FindApiLogsUsecase {

    private final ApiLogRepositoryPort apiLogRepositoryPort;
    private final LoginDomainService loginDomainService;

    @Override
    public List<AnonymousLogResponse> getAnonymousLogs() {
        // 비회원 로그에서 '로그인 시도' 로그를 조회하여 DTO로 변환
        return apiLogRepositoryPort.findAnonymousLogs("로그인 시도").stream()
                .map(entity -> {
                    ApiLog log = entity.toDomain(); // 엔티티를 도메인 객체로 변환
                    int failedAttempts = loginDomainService.getFailedAttemptCount(log.getMemberId());
                    return AnonymousLogMapper.toDto(log, failedAttempts);
                })
                .toList();
    }

    @Override
    public List<MemberLogResponse> getMemberLogs() {
        // 회원 로그를 조회하여 DTO로 변환
        return apiLogRepositoryPort.findMemberLogs().stream()
                .map(entity -> {
                    ApiLog log = entity.toDomain(); // 엔티티를 도메인 객체로 변환
                    return MemberLogMapper.toDto(log);
                })
                .toList();
    }

    @Override
    public List<ApiLog> getApiLogs() {
        // 모든 로그 조회
        return apiLogRepositoryPort.findAllLogs();
    }

    public void save(ApiLog apiLog) {
        apiLogRepositoryPort.save(apiLog);
    }
}
