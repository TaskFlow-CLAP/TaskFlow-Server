package clap.server.application.port.inbound.log;


import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.domain.model.log.ApiLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FindApiLogsUsecase {
    PageResponse<AnonymousLogResponse> filterAnonymousLogs(FilterLogRequest anonymousLogsRequest, Pageable pageable);
    PageResponse<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable);
}