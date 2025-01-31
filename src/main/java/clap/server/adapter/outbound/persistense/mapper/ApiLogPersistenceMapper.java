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
                .serverIp(anonymousLog.getServerIp())
                .clientIp(anonymousLog.getClientIp())
                .requestUrl(anonymousLog.getRequestUrl())
                .requestMethod(ApiHttpMethod.valueOf(anonymousLog.getRequestMethod()))
                .statusCode(anonymousLog.getStatusCode())
                .customStatusCode(anonymousLog.getCustomStatusCode())
                .request(anonymousLog.getRequest())
                .response(anonymousLog.getResponse())
                .requestAt(anonymousLog.getRequestAt())
                .responseAt(anonymousLog.getResponseAt())
                .loginNickname(nickName != null ? nickName : "UNKNOWN")
                .logType(anonymousLog.getLogType())
                .build();
    }

    public MemberLogEntity mapMemberLogToEntity(MemberLog memberLog, MemberEntity memberEntity) {
        return MemberLogEntity.builder()
                .member(memberEntity)
                .serverIp(memberLog.getServerIp())
                .clientIp(memberLog.getClientIp())
                .requestUrl(memberLog.getRequestUrl())
                .requestMethod(ApiHttpMethod.valueOf(memberLog.getRequestMethod()))
                .statusCode(memberLog.getStatusCode())
                .customStatusCode(memberLog.getCustomStatusCode())
                .request(memberLog.getRequest())
                .response(memberLog.getResponse())
                .requestAt(memberLog.getRequestAt())
                .responseAt(memberLog.getResponseAt())
                .logType(memberLog.getLogType())
                .build();
    }

    public AnonymousLog mapAnonymousLogEntityToDomain(AnonymousLogEntity anonymousLogEntity) {
        return AnonymousLog.builder()
                .logId(anonymousLogEntity.getLogId())
                .serverIp(anonymousLogEntity.getServerIp())
                .clientIp(anonymousLogEntity.getClientIp())
                .requestUrl(anonymousLogEntity.getRequestUrl())
                .requestMethod(anonymousLogEntity.getRequestMethod().name())
                .statusCode(anonymousLogEntity.getStatusCode())
                .customStatusCode(anonymousLogEntity.getCustomStatusCode())
                .request(anonymousLogEntity.getRequest())
                .response(anonymousLogEntity.getResponse())
                .requestAt(anonymousLogEntity.getRequestAt())
                .responseAt(anonymousLogEntity.getResponseAt())
                .logType(anonymousLogEntity.getLogType())
                .loginNickname(anonymousLogEntity.getLoginNickname())
                .build();
    }

    public MemberLog mapMemberLogEntityToDomain(MemberLogEntity memberLogEntity) {
        return MemberLog.builder()
                .logId(memberLogEntity.getLogId())
                .serverIp(memberLogEntity.getServerIp())
                .clientIp(memberLogEntity.getClientIp())
                .requestUrl(memberLogEntity.getRequestUrl())
                .requestMethod(memberLogEntity.getRequestMethod().name())
                .statusCode(memberLogEntity.getStatusCode())
                .customStatusCode(memberLogEntity.getCustomStatusCode())
                .request(memberLogEntity.getRequest())
                .response(memberLogEntity.getResponse())
                .requestAt(memberLogEntity.getRequestAt())
                .responseAt(memberLogEntity.getResponseAt())
                .logType(memberLogEntity.getLogType())
                .member(memberLogEntity.getMember() != null
                        ? memberPersistenceMapper.toDomain(memberLogEntity.getMember())
                        : null)
                .build();
    }

    public ApiLog mapLogEntityToDomain(ApiLogEntity logEntity) {
        return ApiLog.builder()
                .logId(logEntity.getLogId())
                .serverIp(logEntity.getServerIp())
                .clientIp(logEntity.getClientIp())
                .requestUrl(logEntity.getRequestUrl())
                .requestMethod(logEntity.getRequestMethod().name())
                .statusCode(logEntity.getStatusCode())
                .customStatusCode(logEntity.getCustomStatusCode())
                .request(logEntity.getRequest())
                .response(logEntity.getResponse())
                .requestAt(logEntity.getRequestAt())
                .responseAt(logEntity.getResponseAt())
                .logType(logEntity.getLogType())
                .build();
    }
}
