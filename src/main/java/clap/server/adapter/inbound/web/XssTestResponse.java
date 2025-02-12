package clap.server.adapter.inbound.web;

public record XssTestResponse(
    String sanitizedContent
) {}