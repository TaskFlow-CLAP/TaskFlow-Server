package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.log.response.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.response.MemberLogResponse;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;

public class LogMapper {
    public static AnonymousLogResponse toAnonymousLogResponse(AnonymousLog anonymousLog, int failedAttempts) {
        return new AnonymousLogResponse(
                anonymousLog.getLogId(),
                anonymousLog.getLogStatus(),
                anonymousLog.getRequestAt(),
                anonymousLog.getLoginNickname(),
                anonymousLog.getClientIp(),
                anonymousLog.getStatusCode(),
                failedAttempts
        );
    }
    public static MemberLogResponse toMemberLogResponse(MemberLog memberLog) {
        return new MemberLogResponse(
                memberLog.getLogId(),
                memberLog.getLogStatus(),
                memberLog.getRequestAt(),
                memberLog.getMember().getNickname(),
                memberLog.getClientIp(),
                memberLog.getStatusCode()
        );
    }
}
