package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;

public interface MemberProfileUsecase {
    MemberProfileResponse getMemberProfile(Long memberId);
}
