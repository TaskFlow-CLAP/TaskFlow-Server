package clap.server.domain.model.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import clap.server.domain.model.common.BaseTime;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiLog extends BaseTime {
    private Long logId;
    private Integer statusCode;
    private LocalDateTime requestAt;
    private LocalDateTime responseAt;
    private String request;
    private String response;
    private String requestUrl;
    private String clientIp;
    private String serverIp;
    private String customStatusCode;
    private String requestMethod;
    private LogStatus logStatus;
}
