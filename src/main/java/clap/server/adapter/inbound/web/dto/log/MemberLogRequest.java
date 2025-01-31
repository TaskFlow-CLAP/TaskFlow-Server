package clap.server.adapter.inbound.web.dto.log;

import clap.server.adapter.outbound.persistense.entity.log.constant.LogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MemberLogRequest(
        @Schema(description = "검색 기간 (단위: 시간)", example = "1, 24, 168, 730, 2190 (1시간, 24시간, 1주일, 1개월, 3개월)")
        Integer term,
        @NotNull
        List<LogStatus> logStatus,
        @NotNull
        String nickName,
        @NotNull
        String ipAddress
        ) {
}
