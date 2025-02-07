package clap.server.adapter.inbound.web.dto.task.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
public record ApprovalTaskRequest(
        @NotNull
        @Schema(description = "2차 카테고리 ID",
                example = "1 ")
        Long categoryId,

        @NotNull
        @Schema(description = "담당자 ID",
                example = "2")
        Long processorId,

        @Schema(description = "마감 기한",
                example = "2025-02-10T15:30:00")
        LocalDateTime dueDate,

        @Schema(description = "라벨 ID",
                example = "1")
        Long labelId
) {
}
