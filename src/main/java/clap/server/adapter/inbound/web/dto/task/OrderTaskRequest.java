package clap.server.adapter.inbound.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderTaskRequest(
        @Schema(description = "정렬 기준", example = "REQUESTED_AT")
        String sortBy,

        @Schema(description = "정렬 방향 (ASC/DESC)", example = "ASC")
        String sortDirection
) {}
