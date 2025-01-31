package clap.server.application.port.outbound.log;

import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoadLogPort {
    List<ApiLog> findAllLogs();
    Page<AnonymousLog> filterAnonymousLogs(FilterLogRequest anonymousLogRequest, Pageable pageable);

    Page<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable);
}
