package clap.server.application.port.inbound.log;


import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.log.response.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.response.MemberLogResponse;
import org.springframework.data.domain.Pageable;

public interface FindApiLogsUsecase {
    PageResponse<AnonymousLogResponse> filterAnonymousLogs(FilterLogRequest anonymousLogsRequest, Pageable pageable, String sortDirection);
    PageResponse<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable, String sortDirection);
}