package clap.server.application.port.inbound.log;


import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.domain.model.log.ApiLog;

import java.util.List;

public interface FindApiLogsUsecase {
    List<AnonymousLogResponse> getAnonymousLogs();
    List<MemberLogResponse> getMemberLogs();
    List<ApiLog> getApiLogs();
}