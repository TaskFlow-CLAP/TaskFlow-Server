package clap.server.adapter.inbound.web.dto.auth.response;

public record ReissueTokenResponse(
        String accessToken,
        String refreshToken
) {
}
