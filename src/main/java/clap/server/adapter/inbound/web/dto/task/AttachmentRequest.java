package clap.server.adapter.inbound.web.dto.task;


import io.swagger.v3.oas.annotations.media.Schema;

public record AttachmentRequest(
        @Schema(description = "파일 ID", example = "45")
        Long fileId,

        @Schema(description = "파일 URL", example = "https://example.com/file.png")
        String fileUrl
) {
}
