package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;

import clap.server.adapter.outbound.persistense.mapper.ApiLogPersistenceMapper;
import clap.server.adapter.outbound.persistense.mapper.MemberPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.log.AnonymousLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.ApiLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.MemberLogRepository;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.application.port.outbound.log.LoadLogPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;

import lombok.RequiredArgsConstructor;

import java.util.List;

import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ApiLogPersistenceAdapter implements CommandLogPort, LoadLogPort {

    private final AnonymousLogRepository anonymousLogRepository;
    private final MemberLogRepository memberLogRepository;
    private final ApiLogRepository apiLogRepository;
    private final MemberPersistenceMapper memberPersistenceMapper;
    private final ApiLogPersistenceMapper apiLogPersistenceMapper;

    @Override
    public void saveMemberLog(MemberLog memberLog) {
        apiLogRepository.save(apiLogPersistenceMapper.mapLogToMemberLogEntity(memberLog, memberPersistenceMapper.toEntity(memberLog.getMember())));

    }

    @Override
    public void saveAnonymousLog(AnonymousLog anonymousLog){
        apiLogRepository.save(apiLogPersistenceMapper.mapLogToAnonymousLogEntity(anonymousLog, anonymousLog.getLoginNickname()));
    }

    @Override
    public List<ApiLog> findAllLogs() {
        return apiLogRepository.findAll().stream()
                .map(this::mapToDomain) // 엔티티를 도메인 객체로 매핑
                .collect(Collectors.toList());
    }

    @Override
    public List<AnonymousLogEntity> findAnonymousLogs(String logType) {
        return anonymousLogRepository.findByLogType(logType);
    }

    @Override
    public List<MemberLogEntity> findMemberLogs() {
        return memberLogRepository.findAll();
    }
}
