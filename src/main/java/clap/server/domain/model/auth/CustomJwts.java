package clap.server.domain.model.auth;

public record CustomJwts(
        String accessToken,
        String refreshToken
) {
    public static CustomJwts of(String accessToken, String refreshToken) {
        return new CustomJwts(accessToken, refreshToken);
    }
}
