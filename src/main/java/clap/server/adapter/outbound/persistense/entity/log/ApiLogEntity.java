package clap.server.adapter.outbound.persistense.entity.log;

import clap.server.adapter.outbound.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
import clap.server.domain.model.log.ApiLog;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_log")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class ApiLogEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private String serverIp;

    @Column(nullable = false)
    private String clientIp;

    @Column(length = 4096, nullable = false)
    private String requestUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ApiHttpMethod requestMethod;

    @Column(nullable = false)
    private Integer statusCode;

    @Column(nullable = false)
    private String customStatusCode;

    @Column(length = 4096, nullable = false)
    private String request;

    @Column(length = 4096, nullable = false)
    private String response;

    @Column(nullable = false)
    private LocalDateTime requestAt;

    @Column(nullable = false)
    private LocalDateTime responseAt;

    @Column(nullable = false)
    private String logType; //TODO: enum으로 수정

    @Version
    private Long version; // 낙관적 락 관리를 위한 버전

    protected ApiLog.ApiLogBuilder toCommonDomainBuilder() {
        return ApiLog.builder()
                .logId(logId)
                .serverIp(serverIp)
                .clientIp(clientIp)
                .requestUrl(requestUrl)
                .requestMethod(requestMethod.name())
                .statusCode(statusCode)
                .customStatusCode(customStatusCode)
                .request(request)
                .response(response)
                .requestAt(requestAt)
                .responseAt(responseAt)
                .logType(logType);
    }

    public abstract ApiLog toDomain();
}
