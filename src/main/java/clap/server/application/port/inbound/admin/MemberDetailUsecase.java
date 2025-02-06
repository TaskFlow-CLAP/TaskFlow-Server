package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.response.MemberDetailsResponse;

public interface MemberDetailUsecase {
    MemberDetailsResponse getMemberDetail(Long memberId);
}