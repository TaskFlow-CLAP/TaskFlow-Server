package clap.server.adapter.inbound.web.dto.task;

import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull
        String target,
        @NotNull
        String type
) {}
