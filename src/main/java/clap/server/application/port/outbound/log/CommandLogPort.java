package clap.server.application.port.outbound.log;

import clap.server.domain.model.log.ApiLog;

public interface CommandLogPort {
    void save(ApiLog apiLog);
}
