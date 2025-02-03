package clap.server.adapter.inbound.web.dto.task.response;

import java.time.LocalDateTime;

public record AttachmentResponse(
        Long fileId,
        String fileName,
        String fileSize,
        String fileUrl,
        LocalDateTime fileUploadedAt
        ) {}