package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.response.MemberProfileResponse;

public interface MemberProfileUsecase {
    MemberProfileResponse getMemberProfile(Long memberId);
}
