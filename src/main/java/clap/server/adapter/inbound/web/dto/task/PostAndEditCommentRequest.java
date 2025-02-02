package clap.server.adapter.inbound.web.dto.task;


import io.swagger.v3.oas.annotations.media.Schema;

public record PostAndEditCommentRequest(
        @Schema(description = "댓글 내용")
        String content
) {
}
