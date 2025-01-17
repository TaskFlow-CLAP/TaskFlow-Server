package clap.server.domain.model.log;

import clap.server.domain.model.common.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiLog extends BaseTime {
    private Long logId;
    private Integer statusCode;
    private Long memberId;
    private LocalDateTime requestAt;
    private LocalDateTime responseAt;
    private String dtype;
    private String request; 
    private String requestUrl; 
    private String response; 
    private String clientIp; 
    private String customStatusCode;
    private String serverIp;
 }
