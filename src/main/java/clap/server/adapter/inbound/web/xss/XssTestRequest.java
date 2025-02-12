package clap.server.adapter.inbound.web.xss;

import jakarta.validation.constraints.NotNull;

public record XssTestRequest(
    @NotNull
    String content
) {}