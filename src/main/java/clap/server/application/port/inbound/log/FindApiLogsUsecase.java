package clap.server.application.port.inbound.log;


import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.domain.model.log.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FindApiLogsUsecase {
    Page<AnonymousLogResponse> filterAnonymousLogs(FilterLogRequest anonymousLogsRequest, Pageable pageable);
    Page<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable);
    List<ApiLog> getApiLogs();
}