package clap.server.application.port.inbound.admin;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;

public interface RegisterMemberUsecase {
    void registerMember(Long adminId, RegisterMemberRequest request);
}
