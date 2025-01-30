package clap.server.adapter.outbound.persistense;

import clap.server.adapter.outbound.persistense.entity.log.AnonymousLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.ApiLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.MemberLogEntity;
import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogTypeEnum;
import clap.server.adapter.outbound.persistense.entity.member.MemberEntity;
import clap.server.adapter.outbound.persistense.repository.log.AnonymousLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.ApiLogRepository;
import clap.server.adapter.outbound.persistense.repository.log.MemberLogRepository;
import clap.server.application.port.outbound.log.CommandLogPort;
import clap.server.application.port.outbound.log.LoadLogPort;
import clap.server.domain.model.log.ApiLog;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiLogPersistenceAdapter implements CommandLogPort, LoadLogPort {

    private final AnonymousLogRepository anonymousLogRepository;
    private final MemberLogRepository memberLogRepository;
    private final ApiLogRepository apiLogRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void save(ApiLog apiLog) {
        validateApiLog(apiLog);
        ApiLogEntity entity;
        if (LogTypeEnum.LOGIN.equals(apiLog.getLogType())) {
            entity = createAnonymousLogEntity(apiLog);
        } else {
            entity = createMemberLogEntity(apiLog);
        }
        apiLogRepository.save(entity);
    }

    @Override
    public List<ApiLog> findAllLogs() {
        return apiLogRepository.findAll().stream()
                .map(this::mapToDomain) // 엔티티를 도메인 객체로 매핑
                .collect(Collectors.toList());
    }

    @Override
    public List<AnonymousLogEntity> findAnonymousLogs(String logType) {
        return anonymousLogRepository.findByLogType(logType);
    }

    @Override
    public List<MemberLogEntity> findMemberLogs() {
        return memberLogRepository.findAll();
    }


    private void validateApiLog(ApiLog apiLog) {
        if (apiLog.getLogType() == null) {
            throw new IllegalArgumentException("Log type must not be null or empty");
        }
        if (!LogTypeEnum.LOGIN.equals(apiLog.getLogType()) && apiLog.getUserId() == null) {
            throw new IllegalArgumentException("Member ID must not be null for member logs");
        }
    }


    private AnonymousLogEntity createAnonymousLogEntity(ApiLog apiLog) {
        return AnonymousLogEntity.builder()
                .serverIp(apiLog.getServerIp())
                .clientIp(apiLog.getClientIp())
                .requestUrl(apiLog.getRequestUrl())
                .requestMethod(ApiHttpMethod.valueOf(apiLog.getRequestMethod()))
                .statusCode(apiLog.getStatusCode())
                .customStatusCode(apiLog.getCustomStatusCode())
                .request(apiLog.getRequest())
                .response(apiLog.getResponse())
                .requestAt(apiLog.getRequestAt())
                .responseAt(apiLog.getResponseAt())
                .loginNickname(apiLog.getUserId() != null ? apiLog.getUserId() : "UNKNOWN")
                .logType(apiLog.getLogType())
                .build();
    }

    private MemberLogEntity createMemberLogEntity(ApiLog apiLog) {
        Long memberId = parseMemberId(apiLog.getUserId());

        //TODO: member 가져오도록 수정 -> 영속화
        // 이미 존재하는 memberId로 MemberEntity를 조회
        MemberEntity newMember = entityManager.find(MemberEntity.class, memberId);

        return MemberLogEntity.builder()
                .member(newMember)
                .serverIp(apiLog.getServerIp())
                .clientIp(apiLog.getClientIp())
                .requestUrl(apiLog.getRequestUrl())
                .requestMethod(ApiHttpMethod.valueOf(apiLog.getRequestMethod()))
                .statusCode(apiLog.getStatusCode())
                .customStatusCode(apiLog.getCustomStatusCode())
                .request(apiLog.getRequest())
                .response(apiLog.getResponse())
                .requestAt(apiLog.getRequestAt())
                .responseAt(apiLog.getResponseAt())
                .logType(apiLog.getLogType())
                .build();
    }

    private Long parseMemberId(String memberId) {
        try {
            return memberId != null ? Long.parseLong(memberId) : 0L; // 기본값 설정
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid member ID: " + memberId, e);
        }
    }

    private ApiLog mapToDomain(ApiLogEntity entity) {
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
