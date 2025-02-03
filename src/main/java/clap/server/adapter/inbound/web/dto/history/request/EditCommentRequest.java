package clap.server.adapter.inbound.web.dto.history;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EditCommentRequest(
        @Schema(description = "댓글 내용")
        @NotBlank
        String content
) {
}
