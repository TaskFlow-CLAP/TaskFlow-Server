package clap.server.application.mapper;

import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.adapter.inbound.web.dto.auth.MemberInfoResponse;
import clap.server.adapter.inbound.web.dto.auth.ReissueTokenResponse;
import clap.server.domain.model.auth.CustomJwts;
import clap.server.domain.model.member.Member;

public class AuthResponseMapper {
    private AuthResponseMapper() {
        throw new IllegalArgumentException();
    }

    public static LoginResponse toLoginResponse(final String accessToken, final String refreshToken, final Member member) {
        return new LoginResponse(
                accessToken,
                refreshToken,
                toMemberInfoResponse(member)
        );
    }

    public static MemberInfoResponse toMemberInfoResponse(Member member) {
        return new MemberInfoResponse(
                member.getMemberId(),
                member.getMemberInfo().getName(),
                member.getMemberInfo().getNickname(),
                member.getImageUrl(),
                member.getMemberInfo().getRole(),
                member.getStatus()
        );
    }

    public static ReissueTokenResponse toReissueTokenResponse(final CustomJwts jwtTokens) {
        return new ReissueTokenResponse(
                jwtTokens.accessToken(),
                jwtTokens.refreshToken()
        );
    }
}
