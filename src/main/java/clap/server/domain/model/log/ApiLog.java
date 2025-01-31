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
    private String clientIp;
    private String requestUrl;
    private String requestMethod;
    private Integer statusCode;
    private String customStatusCode;
    private String requestBody;
    private String responseBody;
    private LocalDateTime requestAt;
    private LogStatus logStatus;
}
