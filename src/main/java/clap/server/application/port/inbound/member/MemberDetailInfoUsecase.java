package clap.server.application.port.inbound.member;

import clap.server.adapter.inbound.web.dto.member.response.MemberDetailInfoResponse;

public interface MemberDetailInfoUsecase {
    MemberDetailInfoResponse getMemberInfo(Long memberId);
}
