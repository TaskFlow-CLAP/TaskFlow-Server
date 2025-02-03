package clap.server.adapter.inbound.web.dto.auth;

public record ReissueTokenResponse(
        String accessToken,
        String refreshToken
) {
}
