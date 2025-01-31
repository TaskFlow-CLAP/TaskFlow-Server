package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.log.AnonymousLogResponse;
import clap.server.adapter.inbound.web.dto.log.MemberLogResponse;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.MemberLog;

public class LogMapper {
    public static AnonymousLogResponse toAnonymounsLogResponse(AnonymousLog anonymousLog, int failedAttempts) {
        return new AnonymousLogResponse(
                anonymousLog.getLogId(),
                anonymousLog.getLoginNickname(),
                anonymousLog.getRequestAt(),
                anonymousLog.getResponseAt(),
                anonymousLog.getRequestUrl(),
                anonymousLog.getRequestMethod(),
                anonymousLog.getStatusCode(),
                anonymousLog.getCustomStatusCode(),
                failedAttempts
        );
    }
    public static MemberLogResponse toMemberLogResponse(MemberLog memberLog) {
        return new MemberLogResponse(
                memberLog.getLogId(),
                memberLog.getLogStatus(),
                memberLog.getRequestAt(),
                memberLog.getMember().getNickname(),
                memberLog.getServerIp(),
                memberLog.getStatusCode()
        );
    }
}
