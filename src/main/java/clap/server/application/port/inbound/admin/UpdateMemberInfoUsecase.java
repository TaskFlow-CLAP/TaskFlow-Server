package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.UpdateMemberInfoRequest;

public interface UpdateMemberInfoUsecase {
    void updateMemberInfo(Long adminId, Long memberId, UpdateMemberInfoRequest request);
}
