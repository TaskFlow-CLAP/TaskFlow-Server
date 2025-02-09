package clap.server.adapter.outbound.api.data;

public record EmailTemplate(
        String email,
        String subject,
        String body
) {
}