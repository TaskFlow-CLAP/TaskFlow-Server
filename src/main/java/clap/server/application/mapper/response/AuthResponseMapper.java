package clap.server.application.mapper.response;

import clap.server.adapter.inbound.web.dto.auth.LoginResponse;
import clap.server.adapter.inbound.web.dto.auth.MemberInfoResponse;
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
                member.getMemberInfo().getRole(),
                member.getStatus()
        );
    }
}
