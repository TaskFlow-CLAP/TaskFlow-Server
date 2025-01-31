package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.log.MemberLogRequest;

import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import clap.server.adapter.outbound.persistense.mapper.ApiLogPersistenceMapper;
import clap.server.adapter.outbound.persistense.mapper.MemberPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.log.AnonymousLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.ApiLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.MemberLogRepository;
import clap.server.application.mapper.response.LogMapper;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.application.port.outbound.log.LoadLogPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ApiLogPersistenceAdapter implements CommandLogPort, LoadLogPort {

    private final ApiLogRepository apiLogRepository;
    private final AnonymousLogRepository anonymousLogRepository;
    private final MemberLogRepository memberLogRepository;
    private final ApiLogPersistenceMapper apiLogPersistenceMapper;
    private final MemberPersistenceMapper memberPersistenceMapper;

    @Override
    public void saveMemberLog(MemberLog memberLog) {
        apiLogRepository.save(apiLogPersistenceMapper.mapMemberLogToEntity(memberLog, memberPersistenceMapper.toEntity(memberLog.getMember())));

    }

    @Override
    public void saveAnonymousLog(AnonymousLog anonymousLog){
        apiLogRepository.save(apiLogPersistenceMapper.mapAnonymousLogToEntity(anonymousLog, anonymousLog.getLoginNickname()));
    }

    @Override
    public List<ApiLog> findAllLogs() {
        return apiLogRepository.findAll().stream()
                .map(apiLogPersistenceMapper::mapLogEntityToDomain)
                .toList();
    }

    @Override
    public List<AnonymousLog> findAnonymousLogs() {
        return anonymousLogRepository.findAll().stream()
                .map(apiLogPersistenceMapper::mapAnonymousLogEntityToDomain)
                .toList();
    }

    @Override
    public Page<MemberLogResponse> filterMemberLogs(MemberLogRequest memberLogRequest, Pageable pageable) {
        Page<MemberLog> memberLogs = memberLogRepository.filterMemberLogs(memberLogRequest, pageable)
                .map(apiLogPersistenceMapper::mapMemberLogEntityToDomain);
        return memberLogs.map(LogMapper::toMemberLogResponse);
    }
}
