package clap.server.application.port.outbound.log;

import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;

public interface CommandLogPort {
    void saveMemberLog(MemberLog memberLog);
    void saveAnonymousLog(AnonymousLog anonymousLog);
}
