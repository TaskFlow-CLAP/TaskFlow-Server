package clap.server.adapter.inbound.web.dto.task;


public record AttachmentRequest(
        Long fileId,
        String fileUrl) {
}
