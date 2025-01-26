package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.UpdateMemberInfoRequest;

public interface ManageMemberUsecase {
    void updateMemberInfo(Long memberId, UpdateMemberInfoRequest request);
}
