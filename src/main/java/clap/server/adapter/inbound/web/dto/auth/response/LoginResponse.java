package clap.server.adapter.inbound.web.dto.auth.response;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}

