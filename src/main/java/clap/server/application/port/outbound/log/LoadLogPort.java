package clap.server.application.port.outbound.log;

import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;

import java.util.List;

public interface LoadLogPort {
    List<ApiLog> findAllLogs();
    List<AnonymousLog> findAnonymousLogs();
    List<MemberLog> findMemberLogs();
}
