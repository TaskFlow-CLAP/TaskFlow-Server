package clap.server.adapter.inbound.web.xss;

public record XssTestResponse(
    String sanitizedContent
) {}