package clap.server.adapter.inbound.web.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemberInfoRequest(
        @NotBlank @Schema(description = "이름")
        String name,
        @Schema(description = "아지트 알림 수신 여부")
        boolean agitNotification,
        @Schema(description = "이메일 알림 수신 여부")
        boolean emailNotification,
        @Schema(description = "카카오 워크 알림 수신 여부")
        boolean kakaoWorkNotification
) {
}



