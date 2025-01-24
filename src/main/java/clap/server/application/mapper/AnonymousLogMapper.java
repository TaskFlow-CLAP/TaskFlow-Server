package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.admin.AnonymousLogResponse;
import clap.server.domain.model.log.ApiLog;

public class AnonymousLogMapper {

    public static AnonymousLogResponse toDto(ApiLog log, Integer failedAttempts) {
        return new AnonymousLogResponse(
                log.getLogId(),
                log.getMemberId(), // 비회원 닉네임
                log.getRequestAt(),
                log.getResponseAt(),
                log.getRequestUrl(),
                log.getRequestMethod(),
                log.getStatusCode(),
                log.getCustomStatusCode(),
                failedAttempts != null ? failedAttempts : 0 // 실패 시도 수 기본값
        );
    }
}
