package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.UpdateMemberRequest;

public interface UpdateMemberUsecase {
    void updateMemberInfo(Long adminId, Long memberId, UpdateMemberRequest request);
}
