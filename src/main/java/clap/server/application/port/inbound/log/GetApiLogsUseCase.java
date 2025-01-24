package clap.server.application.port.inbound.log;


import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.admin.MemberLogResponse;
import clap.server.domain.model.log.ApiLog;

import java.util.List;

public interface GetApiLogsUseCase {
    List<AnonymousLogResponse> getAnonymousLogs();
    List<MemberLogResponse> getMemberLogs();
    List<ApiLog> getApiLogs();
}