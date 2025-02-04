package clap.server.application.port.outbound.log;

import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoadLogPort {
    Page<AnonymousLog> filterAnonymousLogs(FilterLogRequest anonymousLogRequest, Pageable pageable, String sortDirection);

    Page<MemberLog> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable, String sortDirection);
}
