package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.inbound.web.dto.log.MemberLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberLogCustomRepository {
    Page<MemberLogEntity> filterMemberLogs(MemberLogRequest memberLogRequest, Pageable pageable);
}
