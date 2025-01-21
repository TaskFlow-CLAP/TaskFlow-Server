package clap.server.adapter.inbound.web.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateTaskRequest(
        @NotNull
        Long categoryId,
        @NotNull
        Long mainCategoryId,
        @NotBlank
        String title,
        String description,
        List<@NotBlank String> fileUrls
) {
}
