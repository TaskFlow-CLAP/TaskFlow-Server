package clap.server.adapter.outbound.persistense.entity.log;

import clap.server.adapter.outbound.persistense.entity.common.BaseTimeEntity;
import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
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

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String requestBody;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String responseBody;

    @Column(nullable = false)
    private LocalDateTime requestAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LogStatus logStatus;

    @Version
    private Long version;
}
