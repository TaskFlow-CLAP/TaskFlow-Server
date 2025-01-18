package clap.server.application.port.inbound.management;

import clap.server.adapter.inbound.web.dto.admin.RegisterMemberRequest;

public interface RegisterMemberUsecase {
    void registerMember(Long adminId, RegisterMemberRequest request);
}
