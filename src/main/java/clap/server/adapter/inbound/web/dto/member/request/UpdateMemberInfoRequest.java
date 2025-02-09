package clap.server.adapter.inbound.web.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberInfoRequest(
        @NotBlank @Schema(description = "이름")
        String name,
        @NotNull @Schema(description = "이미지 수정이 있을 시에는 false을, 이미지를 삭제할 때에는 true을 보냅니다.")
        Boolean isProfileImageDeleted,
        @Schema(description = "이메일 알림 수신 여부")
        boolean emailNotification,
        @Schema(description = "카카오 워크 알림 수신 여부")
        boolean kakaoWorkNotification
) {
}



