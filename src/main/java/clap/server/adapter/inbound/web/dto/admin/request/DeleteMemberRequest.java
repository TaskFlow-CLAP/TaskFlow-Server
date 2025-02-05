package clap.server.adapter.inbound.web.dto.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DeleteMemberRequest(
        @NotNull(message = "회원 ID는 필수 값입니다.")
        @Schema(description = "삭제할 회원 ID", example = "1", required = true)
        Long memberId
) {
}
