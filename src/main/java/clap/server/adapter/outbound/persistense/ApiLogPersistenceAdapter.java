package clap.server.adapter.outbound.persistense;

import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.adapter.outbound.persistense.mapper.ApiLogPersistenceMapper;
import clap.server.adapter.outbound.persistense.mapper.MemberPersistenceMapper;
import clap.server.adapter.outbound.persistense.repository.log.AnonymousLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.ApiLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.MemberLogRepository;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.application.port.outbound.log.LoadLogPort;
import clap.server.common.annotation.architecture.PersistenceAdapter;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@PersistenceAdapter
@RequiredArgsConstructor
public class ApiLogPersistenceAdapter implements CommandLogPort, LoadLogPort {

    private final ApiLogRepository apiLogRepository;
    private final AnonymousLogRepository anonymousLogRepository;
    private final MemberLogRepository memberLogRepository;
    private final ApiLogPersistenceMapper apiLogPersistenceMapper;
    private final MemberPersistenceMapper memberPersistenceMapper;

    @Transactional
    @Override
    public void saveMemberLog(final MemberLog memberLog) {
        apiLogRepository.save(apiLogPersistenceMapper.mapMemberLogToEntity(memberLog, memberPersistenceMapper.toEntity(memberLog.getMember())));

    }

    @Override
    public void saveAnonymousLog(final AnonymousLog anonymousLog) {
        apiLogRepository.save(apiLogPersistenceMapper.mapAnonymousLogToEntity(anonymousLog, anonymousLog.getLoginNickname()));
    }


    @Override
    public Page<MemberLog> filterMemberLogs(final FilterLogRequest memberLogRequest, final Pageable pageable, final String sortDirection) {
        return memberLogRepository.filterMemberLogs(memberLogRequest, pageable, sortDirection)
                .map(apiLogPersistenceMapper::mapMemberLogEntityToDomain);
    }

    @Override
    public Page<AnonymousLog> filterAnonymousLogs(final FilterLogRequest anonymousLogRequest, final Pageable pageable, final String sortDirection) {
        return anonymousLogRepository.filterAnonymousLogs(anonymousLogRequest, pageable, sortDirection)
                .map(apiLogPersistenceMapper::mapAnonymousLogEntityToDomain);
    }
}
