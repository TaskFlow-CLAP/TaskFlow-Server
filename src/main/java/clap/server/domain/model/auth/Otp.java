package clap.server.domain.model.auth;

public record Otp(
        String email,
        String code
) {
}
