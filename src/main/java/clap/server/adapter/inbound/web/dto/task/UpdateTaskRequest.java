package clap.server.adapter.inbound.web.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateTaskRequest(
        @NotNull
        Long taskId,
        @NotNull
        Long categoryId,
        @NotNull
        Long mainCategoryId,
        @NotBlank
        String title,
        String description,
        List<AttachmentRequest> attachmentRequests
) {
}
