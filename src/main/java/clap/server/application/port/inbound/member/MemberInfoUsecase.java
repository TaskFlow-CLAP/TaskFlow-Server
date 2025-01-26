package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.MemberProfileResponse;

public interface MemberInfoUsecase {
    MemberProfileResponse getMemberProfile(Long memberId);
}
