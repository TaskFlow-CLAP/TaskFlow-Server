package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.ApiLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.domain.model.log.AnonymousLog;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiLogPersistenceMapper {
    private final MemberPersistenceMapper memberPersistenceMapper;
    public AnonymousLogEntity mapAnonymousLogToEntity(ApiLog anonymousLog, String nickName) {
        return AnonymousLogEntity.builder()
                .clientIp(anonymousLog.getClientIp())
                .requestUrl(anonymousLog.getRequestUrl())
                .requestMethod(ApiHttpMethod.valueOf(anonymousLog.getRequestMethod()))
                .statusCode(anonymousLog.getStatusCode())
                .customStatusCode(anonymousLog.getCustomStatusCode())
                .requestBody(anonymousLog.getRequestBody())
                .responseBody(anonymousLog.getResponseBody())
                .requestAt(anonymousLog.getRequestAt())
                .loginNickname(nickName != null ? nickName : "UNKNOWN")
                .logStatus(anonymousLog.getLogStatus())
                .build();
    }

    public MemberLogEntity mapMemberLogToEntity(MemberLog memberLog, MemberEntity memberEntity) {
        return MemberLogEntity.builder()
                .member(memberEntity)
                .clientIp(memberLog.getClientIp())
                .requestUrl(memberLog.getRequestUrl())
                .requestMethod(ApiHttpMethod.valueOf(memberLog.getRequestMethod()))
                .statusCode(memberLog.getStatusCode())
                .customStatusCode(memberLog.getCustomStatusCode())
                .requestBody(memberLog.getRequestBody())
                .responseBody(memberLog.getResponseBody())
                .requestAt(memberLog.getRequestAt())
                .logStatus(memberLog.getLogStatus())
                .build();
    }

    public AnonymousLog mapAnonymousLogEntityToDomain(AnonymousLogEntity anonymousLogEntity) {
        return AnonymousLog.builder()
                .logId(anonymousLogEntity.getLogId())
                .clientIp(anonymousLogEntity.getClientIp())
                .requestUrl(anonymousLogEntity.getRequestUrl())
                .requestMethod(anonymousLogEntity.getRequestMethod().name())
                .statusCode(anonymousLogEntity.getStatusCode())
                .customStatusCode(anonymousLogEntity.getCustomStatusCode())
                .requestBody(anonymousLogEntity.getRequestBody())
                .responseBody(anonymousLogEntity.getResponseBody())
                .requestAt(anonymousLogEntity.getRequestAt())
                .logStatus(anonymousLogEntity.getLogStatus())
                .loginNickname(anonymousLogEntity.getLoginNickname())
                .build();
    }

    public MemberLog mapMemberLogEntityToDomain(MemberLogEntity memberLogEntity) {
        return MemberLog.builder()
                .logId(memberLogEntity.getLogId())
                .clientIp(memberLogEntity.getClientIp())
                .requestUrl(memberLogEntity.getRequestUrl())
                .requestMethod(memberLogEntity.getRequestMethod().name())
                .statusCode(memberLogEntity.getStatusCode())
                .customStatusCode(memberLogEntity.getCustomStatusCode())
                .requestBody(memberLogEntity.getRequestBody())
                .responseBody(memberLogEntity.getResponseBody())
                .requestAt(memberLogEntity.getRequestAt())
                .logStatus(memberLogEntity.getLogStatus())
                .member(memberLogEntity.getMember() != null
                        ? memberPersistenceMapper.toDomain(memberLogEntity.getMember())
                        : null)
                .build();
    }

    public ApiLog mapLogEntityToDomain(ApiLogEntity logEntity) {
        return ApiLog.builder()
                .logId(logEntity.getLogId())
                .clientIp(logEntity.getClientIp())
                .requestUrl(logEntity.getRequestUrl())
                .requestMethod(logEntity.getRequestMethod().name())
                .statusCode(logEntity.getStatusCode())
                .customStatusCode(logEntity.getCustomStatusCode())
                .requestBody(logEntity.getRequestBody())
                .responseBody(logEntity.getResponseBody())
                .requestAt(logEntity.getRequestAt())
                .logStatus(logEntity.getLogStatus())
                .build();
    }
}
