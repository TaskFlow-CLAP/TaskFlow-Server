package clap.server.application.port.outbound.log;

import clap.server.adapter.inbound.web.dto.log.MemberLogRequest;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoadLogPort {
    List<ApiLog> findAllLogs();
    List<AnonymousLog> findAnonymousLogs();

    Page<MemberLogResponse> filterMemberLogs(MemberLogRequest memberLogRequest, Pageable pageable);
}
