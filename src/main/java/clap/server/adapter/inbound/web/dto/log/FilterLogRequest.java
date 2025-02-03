package clap.server.adapter.inbound.web.dto.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FilterLogRequest(
        @Schema(description = "검색 기간 (단위: 시간)",
                example = "1, 24, 168, 730, 2190 (1시간, 24시간, 1주일, 1개월, 3개월)")
        Integer term,

        @NotNull
        @Schema(description = "로그 상태 목록",
                example = "[\"LOGIN\", \"ASSIGNER_CHANGED\", \"COMMENT_ADDED\"]")
        List<LogStatus> logStatus,

        @NotNull
        @Schema(description = "닉네임",
                example = "john_doe")
        String nickName,

        @NotNull
        @Schema(description = "클라이언트 IP 주소",
                example = "192.168.1.1")
        String clientIp
) {
}
