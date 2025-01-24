package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.admin.MemberLogResponse;
import clap.server.domain.model.log.ApiLog;

public class MemberLogMapper {
    public static MemberLogResponse toDto(ApiLog log) {
        return new MemberLogResponse(
                log.getLogId(),
                Long.valueOf(log.getMemberId()), // memberId는 항상 값을 가져야 함
                log.getRequestAt(),
                log.getResponseAt(),
                log.getRequestUrl(),
                log.getRequestMethod(),
                log.getStatusCode(),
                log.getCustomStatusCode()
        );
    }
}
