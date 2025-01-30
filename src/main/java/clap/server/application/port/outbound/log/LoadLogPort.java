package clap.server.application.port.outbound.log;

import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import clap.server.domain.model.log.ApiLog;

import java.util.List;

public interface LoadLogPort {
    List<ApiLog> findAllLogs();
    List<AnonymousLogEntity> findAnonymousLogs(String logType);
    List<MemberLogEntity> findMemberLogs();
}
