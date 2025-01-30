package clap.server.adapter.outbound.persistense.mapper;

import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.ApiLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.domain.model.log.ApiLog;
import clap.server.domain.model.log.MemberLog;
import org.springframework.stereotype.Component;

@Component
public class ApiLogPersistenceMapper {
    public AnonymousLogEntity mapLogToAnonymousLogEntity(ApiLog anonymousLog, String nickName) {
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
                .loginNickname(nickName != null ? anonymousLog.getUserId() : "UNKNOWN")
                .logType(anonymousLog.getLogType())
                .build();
    }

    public MemberLogEntity mapLogToMemberLogEntity(MemberLog memberLog, MemberEntity memberEntity) {
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

    public ApiLog mapToDomain(ApiLogEntity entity) {
        if (entity instanceof MemberLogEntity memberLogEntity) {
            return ApiLog.builder()
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
                    .userId(memberLogEntity.getMember().getMemberId().toString()) // 회원 ID
                    .logType(memberLogEntity.getLogType())
                    .build();
        } else if (entity instanceof AnonymousLogEntity anonymousLogEntity) {
            return ApiLog.builder()
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
                    .userId(anonymousLogEntity.getLoginNickname()) // 로그인 시도 ID
                    .logType(anonymousLogEntity.getLogType())
                    .build();
        }
        throw new IllegalStateException("Unknown log entity type");
    }
}
