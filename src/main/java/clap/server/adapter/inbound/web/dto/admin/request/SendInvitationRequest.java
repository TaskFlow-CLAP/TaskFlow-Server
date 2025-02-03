package clap.server.adapter.inbound.web.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SendInvitationRequest(
        @Schema(description = "회원 ID", required = true)
        @NotNull Long memberId
) {}
