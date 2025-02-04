package clap.server.adapter.inbound.web.dto.history.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @Schema(description = "댓글 내용")
        @NotBlank
        String content
) {
}
