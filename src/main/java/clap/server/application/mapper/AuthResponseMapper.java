package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.auth.response.LoginResponse;
import clap.server.adapter.inbound.web.dto.auth.response.MemberInfoResponse;
import clap.server.adapter.inbound.web.dto.auth.response.ReissueTokenResponse;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.member.Member;

public class AuthResponseMapper {
    private AuthResponseMapper() {
        throw new IllegalArgumentException();
    }

    public static LoginResponse toLoginResponse(final String accessToken, final String refreshToken) {
        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }

    public static ReissueTokenResponse toReissueTokenResponse(final CustomJwts jwtTokens) {
        return new ReissueTokenResponse(
                jwtTokens.accessToken(),
                jwtTokens.refreshToken()
        );
    }
}
