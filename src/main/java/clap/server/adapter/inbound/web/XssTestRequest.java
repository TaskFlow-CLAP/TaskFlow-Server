package clap.server.adapter.inbound.web;

import jakarta.validation.constraints.NotNull;

public record XssTestRequest(
    @NotNull
    String content
) {}