package clap.server.adapter.outbound.persistense.repository.log;

import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnonymousLogCustomRepository {
    Page<AnonymousLogEntity> filterAnonymousLogs(FilterLogRequest anonymousLogRequest, Pageable pageable);
}
