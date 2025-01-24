package clap.server.adapter.inbound.web.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderRequest(
        @Schema(description = "정렬 기준 (REQUESTED_AT/FINISHED_AT)", example = "REQUESTED_AT")
        String sortBy,

        @Schema(description = "정렬 방향 (ASC/DESC)", example = "ASC")
        String sortDirection
) {}
