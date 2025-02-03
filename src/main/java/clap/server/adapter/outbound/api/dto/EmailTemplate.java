package clap.server.adapter.outbound.api.dto;

public record EmailTemplate(
        String email,
        String subject,
        String body
) {
}