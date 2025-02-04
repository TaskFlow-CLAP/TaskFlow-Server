package clap.server.adapter.inbound.web.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        @Schema(description = "회원 정보")
        MemberInfoResponse memberInfo
) {}

