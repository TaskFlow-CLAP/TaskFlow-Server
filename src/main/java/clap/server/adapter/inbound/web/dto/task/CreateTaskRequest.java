package clap.server.adapter.inbound.web.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(
        @NotNull
        Long categoryId,
        @NotNull
        Long mainCategoryId,
        @NotBlank
        String title,
        String description,
        @NotBlank
        String fileUrl
) {
}
