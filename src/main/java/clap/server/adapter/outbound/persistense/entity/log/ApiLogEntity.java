package clap.server.adapter.outbound.persistense.entity.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.ApiHttpMethod;
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
@DiscriminatorColumn(name = "DTYPE")
public class ApiLogEntity {
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

}