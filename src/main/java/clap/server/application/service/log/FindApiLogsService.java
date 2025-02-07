package clap.server.application.service.log;

import clap.server.adapter.inbound.web.dto.common.PageResponse;
import clap.server.adapter.inbound.web.dto.log.response.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.request.FilterLogRequest;
import clap.server.adapter.inbound.web.dto.log.response.MemberLogResponse;
import clap.server.application.mapper.response.LogResponseMapper;
import clap.server.application.port.inbound.log.FindApiLogsUsecase;
import clap.server.application.port.outbound.log.LoadLogPort;
import clap.server.common.annotation.architecture.ApplicationService;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindApiLogsService implements FindApiLogsUsecase {
    private final LoadLogPort loadLogPort;

    @Override
    public PageResponse<AnonymousLogResponse> filterAnonymousLogs(FilterLogRequest anonymousLogRequest, Pageable pageable, String sortDirection) {
        Page<AnonymousLog> anonymousLogs = loadLogPort.filterAnonymousLogs(anonymousLogRequest, pageable, sortDirection);
        Page<AnonymousLogResponse> anonymousLogResponses = anonymousLogs.map(anonymousLog -> LogResponseMapper.toAnonymousLogResponse(anonymousLog));
        return PageResponse.from(anonymousLogResponses);
    }

    @Override
    public PageResponse<MemberLogResponse> filterMemberLogs(FilterLogRequest memberLogRequest, Pageable pageable, String sortDirection) {
        Page<MemberLog> memberLogs = loadLogPort.filterMemberLogs(memberLogRequest, pageable, sortDirection);
        Page<MemberLogResponse> memberLogResponses = memberLogs.map(LogResponseMapper::toMemberLogResponse);
        return PageResponse.from(memberLogResponses);
    }
}
